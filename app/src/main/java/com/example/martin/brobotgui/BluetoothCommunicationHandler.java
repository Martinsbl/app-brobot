package com.example.martin.brobotgui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Martin on 31.01.2016.
 */
public class BluetoothCommunicationHandler {

    private MainActivity activity;
    private BluetoothModel model;

    public BluetoothCommunicationHandler(MainActivity activity) {
        this.activity = activity;
        model = activity.getModel();
    }

    public void startCommunication() {
        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);

        model.bluetoothAdapter = bluetoothManager.getAdapter();
        if ((model.bluetoothAdapter == null) || !model.bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
            Toast.makeText(activity, "Bluetooth not enabled.", Toast.LENGTH_SHORT).show();
        }

        BluetoothGattCallbackHandler bluetoothGattCallbackHandler = new BluetoothGattCallbackHandler(activity);
        bluetoothGattCallbackHandler.setGattCallback();
        model.bluetoothScanner = new BluetoothScanner(activity);
        model.bluetoothScanner.ScanForDevices();
    }

    public void bluetoothDisconnect() {
        model.bluetoothGatt.disconnect();
        model.bluetoothGatt.close();
        model.bluetoothCurrentDevice = null;
        model.qikConfigService = null;
        model.qikConfigCharacteristic = null;
        model.qikMotorService = null;
        model.qikSpeedCharacteristic = null;
        model.qikMeasurementsCharacteristic = null;
        model.brobotBatteryService = null;
        model.brobotBatteryCharacteristic = null;
    }


    public void readConfigCharacteristic() {
        if (model.qikConfigCharacteristic != null) {
            model.bluetoothGatt.readCharacteristic(model.qikConfigCharacteristic);
            Log.i(activity.LOG_TAG, "Reading Qik configs.");
        } else {
            Log.i(activity.LOG_TAG, "Tried to read un initiated characteristic.");
        }
    }

    public void readBatteryCharacteristic() {
        if (model.brobotBatteryCharacteristic != null) {
            model.bluetoothGatt.readCharacteristic(model.brobotBatteryCharacteristic);
        }
    }

    public void WriteConfigCharacteristic(QikMotorControl newQikConfig) {
        model.qikConfigCharacteristic.setValue(newQikConfig.getConfig());
        model.bluetoothGatt.writeCharacteristic(model.qikConfigCharacteristic);
    }

    public void WriteSpeedCharacteristic(byte[] speed) {
        model.qikSpeedCharacteristic.setValue(speed);
        model.bluetoothGatt.writeCharacteristic(model.qikSpeedCharacteristic);
    }
}