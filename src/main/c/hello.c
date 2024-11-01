#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char* sayHello(const char* name) {
    char* result = malloc(100);  // 为结果分配内存
    sprintf(result, "Hello %s", name);
    return result;
}