#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <assert.h>


#define handle_error(msg) \
        do { perror(msg); exit(EXIT_FAILURE); } while (0)


typedef struct {
    char *data;
    size_t length;
    size_t reserved;
} Buffer;


Buffer *new_buffer(size_t reserved) {
    Buffer *buffer = malloc(sizeof(Buffer));
    if (buffer == NULL) {
        handle_error("malloc");
    }

    buffer->data = malloc(reserved);
    if (buffer->data == NULL) {
        free(buffer);
        handle_error("malloc");
    }

    buffer->length = 0;
    buffer->reserved = reserved;

    return buffer;
}

void free_buffer(Buffer *buffer) {
    free(buffer->data);
    free(buffer);
}

void append_buffer(Buffer *buffer, char *data, size_t length) {
    if (buffer->length + length > buffer->reserved) {
        while (buffer->length + length > buffer->reserved) {
            buffer->reserved *= 2;
        }

        char *new_data = realloc(buffer->data, buffer->reserved);
        if (new_data == NULL) {
            free_buffer(buffer);
            handle_error("realloc");
        }

        buffer->data = new_data;
    }

    memcpy(buffer->data + buffer->length, data, length);
    buffer->length += length;
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "usage: realloc <repeats>\n");
        exit(EXIT_FAILURE);
    }

    size_t repeats = atoi(argv[1]);
    size_t chunk_size = 1024;

    for (int i = 0; i < repeats; ++i) {

        FILE *src = fopen("test.dat", "r");
        if (src == NULL) {
            fprintf(stderr, "error opening test.dat");
            exit(EXIT_FAILURE);
        }

        FILE *dst = fopen("output.dat", "w");
        if (dst == NULL) {
            fprintf(stderr, "error opening output.dat");
            exit(EXIT_FAILURE);
        }

        char *data = malloc(chunk_size);
        Buffer *buffer = new_buffer(chunk_size);

        size_t bytes = 0;
        while ((bytes = fread(data, 1, chunk_size, src)) > 0) {
            append_buffer(buffer, data, bytes);
        }

        fwrite(buffer->data, buffer->length, 1, dst);

        fclose(src);
        fclose(dst);

        free(data);
        free_buffer(buffer);
    }

    exit(EXIT_SUCCESS);
}
