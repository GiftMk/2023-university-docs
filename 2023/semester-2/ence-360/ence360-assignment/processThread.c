#include <math.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

typedef double MathFunc_t(double);

typedef struct {
    MathFunc_t* func;
    double rangeStart;
    double rangeEnd;
    size_t numSteps;
    double* totalArea;
} ThreadArgs;

double gaussian(double x)
{
    return exp(-(x * x) / 2) / (sqrt(2 * M_PI));
}

double chargeDecay(double x)
{
    if (x < 0) {
        return 0;
    } else if (x < 1) {
        return 1 - exp(-5 * x);
    } else {
        return exp(-(x - 1));
    }
}

#define NUM_THREADS 8
#define NUM_FUNCS 3
static MathFunc_t* const FUNCS[NUM_FUNCS] = { &sin, &gaussian, &chargeDecay };

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

#define MAX_CHILDREN 4
static volatile int numChildren = 0;

void childProcessHandler(int signo)
{
    if (signo == SIGCHLD) {
        numChildren--;
    }
}

// Integrate using the trapezoid method
void* integrateTrap(void* args)
{
    ThreadArgs* threadArgs = (ThreadArgs*)args;
    double rangeSize = threadArgs->rangeEnd - threadArgs->rangeStart;
    double dx = rangeSize / threadArgs->numSteps;
    double area = 0;

    for (size_t i = 0; i < threadArgs->numSteps; i++) {
        double smallx = threadArgs->rangeStart + i * dx;
        double bigx = threadArgs->rangeStart + (i + 1) * dx;

        area += dx * (threadArgs->func(smallx) + threadArgs->func(bigx)) / 2;
    }

    pthread_mutex_lock(&mutex);
    *(threadArgs->totalArea) += area;
    pthread_mutex_unlock(&mutex);

    pthread_exit(NULL);
}

bool getValidInput(double* start, double* end, size_t* numSteps, size_t* funcId)
{
    printf("Query: [start] [end] [numSteps] [funcId]\n");

    size_t numRead = scanf("%lf %lf %zu %zu", start, end, numSteps, funcId);

    return (numRead == 4 && *end >= *start && *numSteps > 0 && *funcId < NUM_FUNCS);
}

int main(void)
{
    double rangeStart;
    double rangeEnd;
    size_t numSteps;
    size_t funcId;
    pthread_t threads[NUM_THREADS];
    ThreadArgs threadArgs[NUM_THREADS];
    double totalArea = 0.0;

    signal(SIGCHLD, childProcessHandler);

    while (getValidInput(&rangeStart, &rangeEnd, &numSteps, &funcId)) {
        pid_t childPid = fork();

        if (childPid == -1) {
            perror("Fork failed");
            exit(1);
        } else if (childPid == 0) {
            for (int i = 0; i < NUM_THREADS; i++) {
                threadArgs[i].func = FUNCS[funcId];
                threadArgs[i].rangeStart = rangeStart + i * (rangeEnd - rangeStart) / NUM_THREADS;
                threadArgs[i].rangeEnd = rangeStart + (i + 1) * (rangeEnd - rangeStart) / NUM_THREADS;
                threadArgs[i].numSteps = numSteps / NUM_THREADS;
                threadArgs[i].totalArea = &totalArea;

                pthread_create(&threads[i], NULL, integrateTrap, &threadArgs[i]);
            }

            for (int i = 0; i < NUM_THREADS; i++) {
                pthread_join(threads[i], NULL);
            }

            printf(
                "The integral of function %zu in range %g to %g is %.10g\n",
                funcId,
                rangeStart,
                rangeEnd,
                totalArea);
            exit(0);
        } else {
            numChildren++;
            if (numChildren >= MAX_CHILDREN) {
                wait(NULL);
            }
        }
    }

    while (numChildren > 0) {
        wait(NULL);
        numChildren--;
    }

    exit(0);
}
