package com.example.martin.brobotgui;

import android.util.Log;

import com.example.martin.brobotgui.MainActivity;

/**
 * Created by Martin on 31.01.2016.
 */
public class QikMotorControl {

    MainActivity activity;
    private final String LOG_TAG = "QikMotorControl";
    byte[] qikConfig = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private byte[] qikSpeed = {0, 0};

    private static final int DEVICE_ID_INDEX = 0;
    private static final int PWM_FREQ_INDEX = 1;
    private static final int STOP_ON_ERROR_INDEX = 2;
    private static final int SERIAL_TIMEOUT_INDEX = 3;
    private static final int M_0_ACCEL_INDEX = 4;
    private static final int M_1_ACCEL_INDEX = 5;
    private static final int M_0_BREAK_DUR_INDEX = 6;
    private static final int M_1_BREAK_DUR_INDEX = 7;
    private static final int M_0_CURRENT_LIM_INDEX = 8;
    private static final int M_1_CURRENT_LIM_INDEX = 9;
    private static final int M_0_CUR_LIM_RESP_INDEX = 10;
    private static final int M_1_CUR_LIM_RESP_INDEX = 11;

    private byte[] qikMeasurements = {0, 0, 0, 0};

    public QikMotorControl(MainActivity activity) {

        this.activity = activity;
    }

    public byte[] getSpeed() {
        return qikSpeed;
    }

    public void setSpeed(int m0Speed, int m1Speed) {
        this.qikSpeed[0] = (byte) m0Speed;
        this.qikSpeed[1] = (byte) m1Speed;
    }

    public byte[] getMeasuredSpeed() {
        byte[] measuredSpeed = {0, 0};
        measuredSpeed[0] = qikMeasurements[0];
        measuredSpeed[1] = qikMeasurements[1];
        return measuredSpeed;
    }

    public double[] getMeasuredCurrent() {
        double[] measuredCurrent = {0, 0};
        measuredCurrent[0] = qikMeasurements[2] * 0.15;
        measuredCurrent[1] = qikMeasurements[3] * 0.15;
        return measuredCurrent;
    }

    public void setMeasurements(byte[] qikMeasurements) {
        this.qikMeasurements = qikMeasurements;
    }

    public byte[] getConfig() {
        return qikConfig;
    }

    public void setConfig(byte[] qikConfig) {
        this.qikConfig = qikConfig;
    }

    public int getDeviceIdValue() {
        return qikConfig[DEVICE_ID_INDEX];
    }

    public void setDeviceIdValue(int deviceIdValue) {
        qikConfig[DEVICE_ID_INDEX] = (byte) deviceIdValue;
    }

    public int getPwmFreqValue() {
        return (int) qikConfig[PWM_FREQ_INDEX];
    }

    public void setPwmFreqValue(int pwmFreqValue) {
        qikConfig[PWM_FREQ_INDEX] = (byte) pwmFreqValue;
    }

    public int getStopOnErrorValue() {
        return (int) qikConfig[STOP_ON_ERROR_INDEX];
    }

    public void setStopOnErrorValue(int stopOnErrorValue) {
        qikConfig[STOP_ON_ERROR_INDEX] = (byte) stopOnErrorValue;
    }

    public int getSerialTimeoutValue() {
        return (int) qikConfig[SERIAL_TIMEOUT_INDEX];
    }

    public void setSerialTimeoutValue(int serialTimeoutValue) {
        qikConfig[SERIAL_TIMEOUT_INDEX] = (byte) serialTimeoutValue;
    }

    public double getSerialTimeoutValuePrototype(int test) {
        byte timeout = (byte) test;//qikConfig[SERIAL_TIMEOUT_INDEX];
        byte L = (byte) (timeout & 0x0F);
        byte M = (byte) ((timeout >> 4) & 0x07);
        double timeoutSecs = 0.25 * L * Math.pow(2, M);
        Log.i(LOG_TAG, "L: " + L + ". M: " + M + ". Timeout: " + Double.toString(timeoutSecs) + ". Test: " + Double.toString(timeoutSecs * 8));
        return timeoutSecs;
    }

    public int getAcceleration() {
        return (int) qikConfig[M_0_ACCEL_INDEX];
    }

    public void setAcceleration(int acceleration) {
        qikConfig[M_0_ACCEL_INDEX] = (byte) acceleration;
        qikConfig[M_1_ACCEL_INDEX] = (byte) acceleration;
    }


    public int getBreakDuration() {
        return (int) qikConfig[M_0_BREAK_DUR_INDEX];
    }

    public void setBreakDuration(int breakDuration) {
        qikConfig[M_0_BREAK_DUR_INDEX] = (byte) breakDuration;
        qikConfig[M_1_BREAK_DUR_INDEX] = (byte) breakDuration;
    }


    public int getCurrentLimit() {
        return (int) qikConfig[M_0_CURRENT_LIM_INDEX];
    }

    public void setCurrentLimit(int currentLimit) {
        qikConfig[M_0_CURRENT_LIM_INDEX] = (byte) currentLimit;
        qikConfig[M_1_CURRENT_LIM_INDEX] = (byte) currentLimit;
    }


    public int getCurrentLimitResponse() {
        return (int) qikConfig[M_0_CUR_LIM_RESP_INDEX];
    }

    public void setCurrentLimitResp(int currentLimitResponse) {
        qikConfig[M_0_CUR_LIM_RESP_INDEX] = (byte) currentLimitResponse;
        qikConfig[M_1_CUR_LIM_RESP_INDEX] = (byte) currentLimitResponse;
    }

    public byte[] getDefaults() {
        byte[] defaults = {10, 0, 7, 0, 0, 0, 0, 0, 0, 0, 4, 4};
        return defaults;
    }

    public void setDefaultValues() {
        byte[] defaultValues = {10, 0, 6, 0, 0, 0, 0, 0, 0, 0, 4, 4};
        setConfig(defaultValues);
    }
}