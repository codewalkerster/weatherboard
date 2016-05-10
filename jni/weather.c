#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <string.h>

#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOG_TAG "weather"

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include <unistd.h>
#include <string.h>
#include <time.h>

int pressure;
int temperature;
int humidity;

float SEALEVELPRESSURE_HPA = 1024.25;

jint Java_com_hardkernel_odroid_weatherboard_MainActivity_openWeatherBoard(JNIEnv* env, jobject obj, jstring str) {
    const char *i2c_dev = (*env)->GetStringUTFChars(env, str, 0);
    int result = -1;
    result = si1132_begin(i2c_dev);
    if (result == -1)
        return result;
    result = bme280_begin(i2c_dev);
    (*env)->ReleaseStringUTFChars(env, str, i2c_dev);
    return result;
}

void Java_com_hardkernel_odroid_weatherboard_MainActivity_closeWeatherBoard(JNIEnv* env, jobject obj) {
    bme280_end();
    si1132_end();
}

void Java_com_hardkernel_odroid_weatherboard_MainActivity_bme280_end(JNIEnv* env, jobject obj) {
    bme280_end();
}

jint Java_com_hardkernel_odroid_weatherboard_MainActivity_getUVindex(JNIEnv* env, jobject obj) {
    int result = 0;
    Si1132_readUV(&result);
    return result;
}

jfloat Java_com_hardkernel_odroid_weatherboard_MainActivity_getVisible(JNIEnv* env, jobject obj) {
    float result = 0.0;
    Si1132_readVisible(&result);
    return result;
}

jfloat Java_com_hardkernel_odroid_weatherboard_MainActivity_getIR(JNIEnv* env, jobject obj) {
    float result = 0.0;
    Si1132_readIR(&result);
    return result;
}

void Java_com_hardkernel_odroid_weatherboard_MainActivity_readyData(JNIEnv* env, jobject obj) {
    bme280_read_pressure_temperature_humidity(&pressure, &temperature, &humidity);
}

jint Java_com_hardkernel_odroid_weatherboard_MainActivity_getTemperature(JNIEnv* env, jobject obj) {
    return temperature;
}

jint Java_com_hardkernel_odroid_weatherboard_MainActivity_getPressure(JNIEnv* env, jobject obj) {
    return pressure;
}

jint Java_com_hardkernel_odroid_weatherboard_MainActivity_getHumidity(JNIEnv* env, jobject obj) {
    return humidity;
}

jint Java_com_hardkernel_odroid_weatherboard_MainActivity_getAltitude(JNIEnv* env, jobject obj) {
    int result = 0;
    bme280_readAltitude(pressure, &SEALEVELPRESSURE_HPA, &result);
    return result;
}
