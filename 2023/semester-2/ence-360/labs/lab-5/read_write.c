#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

#define handle_error(msg) \
        do { perror(msg); exit(EXIT_FAILURE); } while (0)

size_t file_size(int fd) {
  struct stat sb;  
  if (fstat(fd, &sb) == -1) handle_error("fstat");
  
  return sb.st_size;
}

int main(int argc, char *argv[]) {
  if (argc < 3) {
    fprintf(stderr, "usage: read_write <repeats> <chunk_size>\n");
    exit(EXIT_FAILURE);
  }
  
  size_t repeats = atoi(argv[1]);
  size_t chunk_size = atoi(argv[2]);
  
  for(int i = 0; i < repeats; ++i) {
  
    int src = open("test.dat", O_RDONLY);
    if (src == -1) handle_error("open src");

    int dst = open("output.dat", O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
    if (dst == -1) handle_error("open dst");
    
    char *buffer = malloc(chunk_size);
    if (buffer == NULL) handle_error("malloc buffer");

    ssize_t bytes_read;
    ssize_t bytes_written;
    
    while ((bytes_read = read(src, buffer, chunk_size)) > 0) {
      bytes_written = write(dst, buffer, bytes_read);
      if (bytes_written != bytes_read) {
        handle_error("write");
      }
    }

    if (bytes_read == -1) {
      handle_error("read");
    }

    close(src);
    close(dst);
    
    free(buffer);
  }
  
  exit(EXIT_SUCCESS);
}
