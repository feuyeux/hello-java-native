#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "org_feuyeux_java_HelloJni.h"

// Implementation of the native method sayHello()
JNIEXPORT jstring JNICALL Java_org_feuyeux_java_HelloJni_sayHello(JNIEnv *env, jobject thisObj, jstring j_str)
{
    const char *c_str = (*env)->GetStringUTFChars(env, j_str, NULL);
    if (c_str == NULL) {
        return NULL; // Out of memory
    }

    char cs[128] = "Hello ";
    strncat(cs, c_str, sizeof(cs) - strlen(cs) - 1);

    printf("C output: %s\n", cs);

    (*env)->ReleaseStringUTFChars(env, j_str, c_str);

    return (*env)->NewStringUTF(env, cs);
}