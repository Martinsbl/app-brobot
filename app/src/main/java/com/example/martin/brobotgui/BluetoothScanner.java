package com.example.martin.brobotgui;

import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.le.BluetoothLeScanner;

import java.util.List;
import java.util.UUID;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.LogRecord;

/**
 * Created by Martin on 31.01.2016.
 */
public class BluetoothScanner {

    private Handler handler;
    MainActivity activity;
    private BluetoothModel model;
    BluetoothGattCallback bluetoothGattCallback;
    boolean isScanning = false;

    public BluetoothScanner(MainActivity activity) {
        this.activity = activity;
        model = activity.getModel();
        model.bluetoothLeScanner = model.bluetoothAdapter.getBluetoothLeScanner();
        this.bluetoothGattCallback = model.bluetoothGattCallback;
    }

    public void ScanForDevices() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScanning) {
                    isScanning = false;
                    model.bluetoothLeScanner.stopScan(scanCallback);
                    Toast.makeText(activity, "Scanning stopped.", Toast.LENGTH_SHORT).show();
                }
            }
        }, 30000);
        model.bluetoothLeScanner.startScan(scanCallback);
        isScanning = true;
        Toast.makeText(activity, "Scanning started.", Toast.LENGTH_SHORT).show();
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.i(activity.LOG_TAG, "onScanResult. Device found: " + result.getDevice().getName());
            if (result.getDevice().getName() != null) {
                if ((model.bluetoothCurrentDevice == null) && result.getDevice().getName().equals(BluetoothModel.DESIRED_DEVICE_NAME)) {
                    model.bluetoothLeScanner.stopScan(scanCallback);
                    isScanning = false;
                    model.bluetoothCurrentDevice = result.getDevice();
                    model.bluetoothGatt = model.bluetoothCurrentDevice.connectGatt(activity, false, bluetoothGattCallback);
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };
}