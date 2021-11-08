//
// Created by Dibyanshu Anand on 03/11/21.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hotswappoc_MainActivity_secJNIFunc(
        JNIEnv* env,
        jobject) {
    std::string secStr = "This line comes from secLib";
    return env -> NewStringUTF(secStr.c_str());
}