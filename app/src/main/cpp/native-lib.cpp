#include <jni.h>
#include <string>

/*
 * Tips：
 * 1. 快速生成代码，在 Native 方法上 Alt + Enter
 * 2. 新建 C/C++ 源文件要添加到 CMake 中（add_library、target_link_libraries）
 * 3. 引入第三方 so 库，也要添加到 CMake 中（add_library、set_target_properties、target_link_libraries）
 * */

extern "C"
JNIEXPORT jstring JNICALL
Java_com_plant_toyapp_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {

    std::string hello = "Hello from C++";

    return env->NewStringUTF(hello.c_str());
}