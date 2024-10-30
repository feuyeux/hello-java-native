#include <stdio.h>

char* sayHello(char *name)
{
    static char buffer[50];
    snprintf(buffer, sizeof(buffer), "Hello %s", name);
    printf("C output: %s\n", buffer);
    return buffer;
}