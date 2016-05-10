LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES += \
    $(NDK_PATH)/platforms/android-21/arch-arm/usr/include

LOCAL_MODULE    := weather
LOCAL_SRC_FILES := \
    weather.c \
    bme280-i2c.c \
    bme280.c \
    si1132.c
LOCAL_LDLIBS    := -ldl -llog

include $(BUILD_SHARED_LIBRARY)
