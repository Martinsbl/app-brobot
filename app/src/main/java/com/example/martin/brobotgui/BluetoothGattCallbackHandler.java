package com.example.martin.brobotgui;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Martin on 31.01.2016.
 */
public class BluetoothGattCallbackHandler {

    private BluetoothModel model;
    private Brobot brobot;
    private MainActivity activity;

    private enum ConnectionStateMachine{
        STATE_DISCONNECTED,
        STATE_CONNECTED,
        STATE_SERVICE_DISCOVERY,
        STATE_ENABLE_CCCD_MEASUREMENTS,
        STATE_ENABLE_CCCD_BATTERY,
        STATE_READ_INITIAL_CONFIG_CHAR,
        STATE_READ_INITIAL_BATTERY_CHAR,
        STATE_CONNECTION_FINNISHED
    }
    private ConnectionStateMachine connectionState = ConnectionStateMachine.STATE_DISCONNECTED;

    public BluetoothGattCallbackHandler(MainActivity activity) {
        this.activity = activity;
        this.model = activity.getModel();
        this.brobot = activity.getBrobot();
    }

    public void setGattCallback() {
        model.bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Connected to " + gatt.getDevice().getName(), Toast.LENGTH_SHORT).show();
                            activity.connectMainActivity();
                        }
                    });
                    Log.i(activity.LOG_TAG, "Connected to " + gatt.getDevice().getName());
                    model.bluetoothGatt.discoverServices();
                    connectionState = ConnectionStateMachine.STATE_CONNECTED;
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Disconnected from " + gatt.getDevice().getName(), Toast.LENGTH_SHORT).show();
                            activity.disconnectMainActivity();
                        }
                    });
                    model.bluetoothCommunicationHandler.bluetoothDisconnect();

                    connectionState = ConnectionStateMachine.STATE_DISCONNECTED;
                } else if (newState == BluetoothGatt.STATE_DISCONNECTING) {
                    Log.i(activity.LOG_TAG, "DisconnectING");
                }

            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.i(activity.LOG_TAG, "onServiceDiscovered: Found: " + gatt.getServices().size() + " services.");
                int i;
                for (i = 0; i < gatt.getServices().size(); i++) {
                    Log.i(activity.LOG_TAG, "Service " + i + ": " + gatt.getServices().get(i).getUuid());
                    if (BluetoothModel.BLE_UUID_QIK_CONFIG_SERVICE.equals(gatt.getServices().get(i).getUuid())) {
                        model.qikConfigService = gatt.getServices().get(i);
                    } else if (BluetoothModel.BLE_UUID_QIK_MOTOR_SERVICE.equals(gatt.getServices().get(i).getUuid())) {
                        model.qikMotorService = gatt.getServices().get(i);
                    } else if (BluetoothModel.BLE_UUID_BATTERY_SERVICE.equals(gatt.getServices().get(i).getUuid())) {
                        model.brobotBatteryService = gatt.getServices().get(i);
                    }

                }

                if (model.qikConfigService != null) {
                    int j;
                    for (j = 0; j < model.qikConfigService.getCharacteristics().size(); j++) {
                        if (BluetoothModel.BLE_UUID_QIK_CONFIG_CHAR.equals(model.qikConfigService.getCharacteristics().get(j).getUuid())) {
                            model.qikConfigCharacteristic = model.qikConfigService.getCharacteristics().get(j);
                            if (model.bluetoothGatt.setCharacteristicNotification(model.qikConfigCharacteristic, true)) {
                                BluetoothGattDescriptor cccdDescriptor = model.qikConfigCharacteristic.getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                                cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                if (model.bluetoothGatt.writeDescriptor(cccdDescriptor)) {
                                    Log.i(activity.LOG_TAG, "CCCD enabled.");
                                } else
                                    Log.i(activity.LOG_TAG, "CCCD failed to enable.");
                            }
                            //activity.setBrobot();
                            //activity.brobot = new Brobot();
                            brobot.qikMotorControl = new QikMotorControl(activity);
                        }
                    }
                }
                if (model.qikMotorService != null) {
                    int j;
                    for (j = 0; j < model.qikMotorService.getCharacteristics().size(); j++) {
                        if (BluetoothModel.BLE_UUID_QIK_SPEED_CHAR.equals(model.qikMotorService.getCharacteristics().get(j).getUuid())) {
                            model.qikSpeedCharacteristic = model.qikMotorService.getCharacteristics().get(j);
                            Log.i(activity.LOG_TAG, "Characteristic found.");
                        } else if (BluetoothModel.BLE_UUID_QIK_MEASUREMENTS_CHAR.equals(model.qikMotorService.getCharacteristics().get(j).getUuid())) {
                            Log.i(activity.LOG_TAG, "BLE_UUID_QIK_MEASUREMENTS_CHAR found.");
                            model.qikMeasurementsCharacteristic = model.qikMotorService.getCharacteristics().get(j);
                        }
                    }
                }
                if (model.brobotBatteryService != null) {
                    int j;
                    for (j = 0; j < model.brobotBatteryService.getCharacteristics().size(); j++) {
                        if (BluetoothModel.BLE_UUID_BATTERY_CHAR.equals(model.brobotBatteryService.getCharacteristics().get(j).getUuid())) {
                            model.brobotBatteryCharacteristic = model.brobotBatteryService.getCharacteristics().get(j);
                            Log.i(activity.LOG_TAG, "Battery Characteristic found.");
                        }
                    }
                }

                connectionState = ConnectionStateMachine.STATE_ENABLE_CCCD_MEASUREMENTS;
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                if (characteristic.getUuid().equals(BluetoothModel.BLE_UUID_QIK_CONFIG_CHAR)) {
                    brobot.qikMotorControl.setConfig(characteristic.getValue());
                    Log.i(activity.LOG_TAG, "QikConfig: " + brobot.qikMotorControl.getDeviceIdValue());
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            activity.updateQikConfigGuiValues();
//                        }
//                    });
                    model.bluetoothCommunicationHandler.readBatteryCharacteristic(); // DETTE BØR GJØRES PÅ EN ANNEN MÅTE!
                } else if (characteristic.getUuid().equals(BluetoothModel.BLE_UUID_BATTERY_CHAR)) {
                    brobot.batteryLevel = (characteristic.getValue()[1] << 8 ) + (characteristic.getValue()[0]);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.setBatteryVoltageMainActivity();
                        }
                    });
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                if (characteristic.getUuid().equals(BluetoothModel.BLE_UUID_QIK_CONFIG_CHAR)) {
                    brobot.qikMotorControl.setConfig(characteristic.getValue());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.updateSetupFragment();
                        }
                    });
                }else if (characteristic.getUuid().equals(BluetoothModel.BLE_UUID_QIK_MEASUREMENTS_CHAR)) {
                    brobot.qikMotorControl.setMeasurements(characteristic.getValue());
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            activity.updateQikMeasurementsGuiValues();
//                        }
//                    });
                }else if (characteristic.getUuid().equals(BluetoothModel.BLE_UUID_BATTERY_CHAR)) {
                    brobot.batteryLevel = (characteristic.getValue()[1] << 8 ) + (characteristic.getValue()[0]);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.setBatteryVoltageMainActivity();
                        }
                    });
                }
                model.bluetoothGatt.readRemoteRssi();
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                Log.i(activity.LOG_TAG, "onDescriptorWrite");
                switch (connectionState) {
                    case STATE_ENABLE_CCCD_MEASUREMENTS:
                        if (model.bluetoothGatt.setCharacteristicNotification(model.qikMeasurementsCharacteristic, true)) {
                            BluetoothGattDescriptor cccdDescriptor = model.qikMeasurementsCharacteristic.getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                            cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            if (model.bluetoothGatt.writeDescriptor(cccdDescriptor)) {
                                Log.i(activity.LOG_TAG, "Measurements CCCD enabled.");
                            } else
                                Log.i(activity.LOG_TAG, "Measurements CCCD failed to enable.");
                        }
                        connectionState = ConnectionStateMachine.STATE_ENABLE_CCCD_BATTERY;
                        return;
                    case STATE_ENABLE_CCCD_BATTERY:
                        if (model.bluetoothGatt.setCharacteristicNotification(model.brobotBatteryCharacteristic, true)) {
                            BluetoothGattDescriptor cccdDescriptor = model.brobotBatteryCharacteristic.getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                            cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            if (model.bluetoothGatt.writeDescriptor(cccdDescriptor)) {
                                Log.i(activity.LOG_TAG, "Battery CCCD enabled.");
                            } else
                                Log.i(activity.LOG_TAG, "Battery CCCD failed to enable.");
                        }
                        connectionState = ConnectionStateMachine.STATE_READ_INITIAL_CONFIG_CHAR;
                        return;
                    case STATE_READ_INITIAL_CONFIG_CHAR:
                        if (descriptor.getUuid().equals(BluetoothModel.BLE_UUID_CCCD)) {
                            model.bluetoothCommunicationHandler.readConfigCharacteristic();
                        }
                        Log.i(activity.LOG_TAG, "Read init config.");
                        connectionState = ConnectionStateMachine.STATE_CONNECTION_FINNISHED;
                        return;
                    default:
                        break;
                }
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                model.rssi = rssi;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setSignalStrengthIconMainActivity();
                    }
                });
            }
        };
    }
}