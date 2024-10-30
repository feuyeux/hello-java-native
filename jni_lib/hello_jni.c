#include <jni.h>      // JNI header provided by JDK
#include <stdio.h>    // C Standard IO Header
#include "HelloJni.h" // Generated

// Implementation of the native method sayHello()
JNIEXPORT jstring JNICALL Java_HelloJni_sayHello(JNIEnv *env, jobject thisObj, jstring j_str)
{
    const jchar *c_str = NULL;
    char cs[128] = "Hello ";
    char *pBuff = cs + 6;
    c_str = (*env)->GetStringCritical(env, j_str, NULL);
    if (c_str != NULL)
    {
        while (*c_str)
        {
            *pBuff++ = *c_str++;
        }
        (*env)->ReleaseStringCritical(env, j_str, c_str);
        printf("C output:%s\n", cs);
    }
    return (*env)->NewStringUTF(env, cs);
}