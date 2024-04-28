#include <stdio.h>

int main() {
  char c;

  puts("Enter a character: ");
  while ((c = getchar()) != EOF) {
    printf("You entered %c \n", c);
  }

  puts("Recieved EOF");

  return 0;
}