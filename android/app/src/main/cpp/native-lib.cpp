#include <jni.h>
#include <string>
#include "DataManager.h"
#ifdef __cplusplus
extern "C" {
#endif

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

JNIEXPORT void JNICALL
Java_com_raptware_docdigitales_JNI_StartDataBase(JNIEnv* env, jobject /* this */,jstring FileName){
    DataManager::GetInstance().OpenData(jstring2string(env,FileName));
}

JNIEXPORT void JNICALL
Java_com_raptware_docdigitales_JNI_CloseDataBase(JNIEnv* env, jobject /* this */){
    DataManager::GetInstance().CloseData();
}

JNIEXPORT jboolean JNICALL
Java_com_raptware_docdigitales_JNI_CreateRegister(JNIEnv* env, jobject /* this */,
        jstring FullName, jstring Email, jstring RFC, jstring EnterpriseName, jstring Password) {
    return (jboolean)DataManager::GetInstance().CreateRegister(jstring2string(env,FullName),jstring2string(env,Email),jstring2string(env,RFC),jstring2string(env,EnterpriseName),jstring2string(env,Password));
}

JNIEXPORT jint JNICALL
Java_com_raptware_docdigitales_JNI_CreateBranch(JNIEnv* env, jobject /* this */,
        jstring UserEmail, jstring BranchName, jstring Street, jstring Colony,jstring Number, jstring PostalCode, jstring City, jstring Country) {
    return (jint)DataManager::GetInstance().CreateBranch(jstring2string(env,UserEmail),jstring2string(env,BranchName),jstring2string(env,Street),jstring2string(env,Colony),jstring2string(env,Number),jstring2string(env,PostalCode),jstring2string(env,City),jstring2string(env,Country));
}

JNIEXPORT jboolean JNICALL
Java_com_raptware_docdigitales_JNI_SaveBranch(JNIEnv* env, jobject /* this */,
        jstring id, jstring BranchName, jstring Street, jstring Colony,jstring Number, jstring PostalCode, jstring City, jstring Country) {
    return (jint)DataManager::GetInstance().SaveBranch(jstring2string(env,id),jstring2string(env,BranchName),jstring2string(env,Street),jstring2string(env,Colony),jstring2string(env,Number),jstring2string(env,PostalCode),jstring2string(env,City),jstring2string(env,Country));
}

JNIEXPORT jboolean JNICALL
Java_com_raptware_docdigitales_JNI_Login(JNIEnv* env, jobject /* this */,
        jstring Email, jstring Password) {
    return (jboolean)DataManager::GetInstance().Login(jstring2string(env,Email),jstring2string(env,Password));
}

JNIEXPORT jstring JNICALL
Java_com_raptware_docdigitales_JNI_GetUserInfo(JNIEnv* env, jobject /* this */,
        jstring Email) {
    return env->NewStringUTF(DataManager::GetInstance().GetUserInfo(jstring2string(env,Email)));
}

JNIEXPORT jstring JNICALL
Java_com_raptware_docdigitales_JNI_GetBranchesByUser(JNIEnv* env, jobject /* this */,
        jstring Email) {
    return env->NewStringUTF(DataManager::GetInstance().GetBranchesByUser(jstring2string(env,Email)));
}
JNIEXPORT jstring JNICALL
Java_com_raptware_docdigitales_JNI_GetBranchByID(JNIEnv* env, jobject /* this */,
        jstring id) {
    return env->NewStringUTF(DataManager::GetInstance().GetBranchByID(jstring2string(env,id)));
}
#ifdef __cplusplus
}
#endif