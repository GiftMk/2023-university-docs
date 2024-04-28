#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

#define NUM_WORKERS 10

typedef struct Worker {
  pthread_mutex_t *mutex;
  sem_t *semaphore;
  struct Worker *buddy;
  pthread_t thread;
} Worker;

int global = 0;

void increment_global(Worker *worker) {
  pthread_mutex_lock(worker->mutex);
  global++;
  pthread_mutex_unlock(worker->mutex);
  sem_post(worker->semaphore);
}

void *childCode(void *ptr) {
  Worker *worker = (Worker*)ptr;
  Worker *buddy = worker->buddy;

  sem_wait(buddy->semaphore);
  increment_global(worker);

  return NULL;
}

int main() {
  pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
  Worker workers[NUM_WORKERS];

  for (int i = 0; i < NUM_WORKERS; i++) {
    Worker *worker = &workers[i];
    worker->mutex = & mutex;

    char semaphore_name[30];
    snprintf(semaphore_name, sizeof(semaphore_name), "/semaphore%d", i);

    if (i == 0) {
      worker->semaphore = sem_open(semaphore_name, O_CREAT, 0644, 1);
      worker->buddy = NULL;
    } else {
      worker->semaphore = sem_open(semaphore_name, O_CREAT, 0644, 0);
      worker->buddy = &worker[i - 1];
    }

    if (worker->semaphore == SEM_FAILED) {
      perror("Semaphore creation failed");
      exit(EXIT_FAILURE);
    }

    pthread_create(&worker->thread, NULL, childCode, worker);
  }

  for (int i = 0; i < NUM_WORKERS; i++) {
    pthread_join(workers[i].thread, NULL);
    sem_close(workers[i].semaphore);
  }

  printf("Global: %d\n", global);

  return 0;
}