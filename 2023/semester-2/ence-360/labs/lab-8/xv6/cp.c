#include "fcntl.h"
#include "stat.h"
#include "types.h"
#include "user.h"

char buf[512];

void cp(int src_fd, int dest_fd)
{
    int n;

    while ((n = read(src_fd, buf, sizeof(buf))) > 0) {
        if (write(dest_fd, buf, n) != n) {
            printf(1, "cp: write error\n");
            exit();
        }
    }

    if (n < 0) {
        printf(1, "cp: read error\n");
        exit();
    }
}

int main(int argc, char* argv[])
{
    int src_fd;
    int dest_fd;

    if (argc != 3) {
        printf(1, "usage: cp src_file dest_file\n");
        exit();
    }

    src_fd = argv[1];
    dest_fd = argv[2];

    if (open(src_fd, 0) < 0) {
        printf(1, "cp: cannot open %s\n", src_fd);
        exit();
    }

    if (open(dest_fd, 0) < 0) {
        printf(1, "cp: cannot open %s\n", dest_fd);
        exit();
    }

    cp(src_fd, dest_fd);
    close(src_fd);
    close(dest_fd);

    exit();
}
