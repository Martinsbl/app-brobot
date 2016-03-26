package com.example.martin.brobotgui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;

import java.util.UUID;

/**
 * Created by Martin on 22.02.2016.
 */
public class BluetoothModel {

    BluetoothAdapter bluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    BluetoothGattCallback bluetoothGattCallback;
    BluetoothScanner bluetoothScanner;
    BluetoothLeScanner bluetoothLeScanner;
    BluetoothCommunicationHandler bluetoothCommunicationHandler;
    BluetoothDevice bluetoothCurrentDevice;
    BluetoothGattService qikConfigService;
    BluetoothGattCharacteristic qikConfigCharacteristic;

    BluetoothGattService qikMotorService;
    BluetoothGattCharacteristic qikSpeedCharacteristic;
    BluetoothGattCharacteristic qikMeasurementsCharacteristic;

    BluetoothGattService brobotBatteryService;
    BluetoothGattCharacteristic brobotBatteryCharacteristic;

    int rssi;

    public static final int BROBOT_MAX_SPEED = 90;

    public static final UUID BLE_UUID_CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final UUID BLE_UUID_QIK_CONFIG_SERVICE = UUID.fromString ("0000b0b0-1212-efde-1523-785fef13d123");
    public static final UUID BLE_UUID_QIK_CONFIG_CHAR = UUID.fromString("000005a5-1212-efde-1523-785fef13d123");

    public static final UUID BLE_UUID_QIK_MOTOR_SERVICE = UUID.fromString ("0000E1CE-1212-efde-1523-785fef13d123");
    public static final UUID BLE_UUID_QIK_SPEED_CHAR = UUID.fromString("00005EED-1212-efde-1523-785fef13d123");
    public static final UUID BLE_UUID_QIK_MEASUREMENTS_CHAR = UUID.fromString("0000EA5E-1212-efde-1523-785fef13d123");

    public static final UUID BLE_UUID_BATTERY_SERVICE = UUID.fromString ("0000180F-0000-1000-8000-00805f9b34fb");
    public static final UUID BLE_UUID_BATTERY_CHAR = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");

    public static final String DESIRED_DEVICE_NAME = "Brobot";
}