package com.example.martin.brobotgui;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Martin on 27.02.2016.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    public final String LOG_TAG = "Brobot";
    Button btnConnect;
    Button btnDisconnect;
    Button btnTest1;
    Button btnTest2;
    ImageView imgBrobot;
    BluetoothModel model;
    Brobot brobot;
    MainActivity activity;
    ImageView imgRssi;
    TextView txtRssi;
    TextView coordinates;
    ImageView imgBatteryLevel;
    TextView txtBatteryLevel;
    TextView txtBatteryVoltage;
    Joystick joystick;
    CurrentGauge currentGauge0;
    TextView txtCurrent0;
    CurrentGauge currentGauge1;
    TextView txtCurrent1;

    Timer timerRssiUpdate;
    Timer timerSpeedUpdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        activity = ((MainActivity) getActivity());
        createGui(view);

        model = activity.getModel();
        brobot = activity.getBrobot();
        model.bluetoothCommunicationHandler.startCommunication();

        timerRssiUpdate = new Timer();
        timerRssiUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (model.bluetoothCurrentDevice != null) {
                            model.bluetoothGatt.readRemoteRssi();
                            setSignalStrengthIcon();
                        }
                    }
                });
            }
        }, 0, 2000);

        return view;
    }

    private void createGui(View view) {
        btnConnect = (Button) view.findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);
        btnDisconnect = (Button) view.findViewById(R.id.btn_disconnect);
        btnDisconnect.setOnClickListener(this);
        imgBrobot = (ImageView) view.findViewById(R.id.img_brobot);

        btnTest1 = (Button) view.findViewById(R.id.btn_test1);
        btnTest1.setOnClickListener(this);
        btnTest2 = (Button) view.findViewById(R.id.btn_test2);
        btnTest2.setOnClickListener(this);

        imgRssi = (ImageView) view.findViewById(R.id.img_rssi);
        imgRssi.setImageResource(R.drawable.ic_signal_wifi_off_24dp);
        txtRssi = (TextView) view.findViewById(R.id.txt_rssi);
        coordinates = (TextView) view.findViewById(R.id.coordinates);

        imgBatteryLevel = (ImageView) view.findViewById(R.id.img_battery_level);
        imgRssi.setImageResource(R.drawable.ic_battery_unknown_24dp);
        txtBatteryLevel = (TextView) view.findViewById(R.id.txt_battery_level);
        txtBatteryVoltage = (TextView) view.findViewById(R.id.txt_battery_voltage);

        currentGauge0 = (CurrentGauge) view.findViewById(R.id.current_gauge_0);
        txtCurrent0 = (TextView) view.findViewById(R.id.txt_current0);
        currentGauge1 = (CurrentGauge) view.findViewById(R.id.current_gauge_1);
        txtCurrent1 = (TextView) view.findViewById(R.id.txt_current1);

        joystick = (Joystick) view.findViewById(R.id.joystick);
        joystick.setMainFragment(this);
    }

    public void setSignalStrengthIcon() {
        int maxRssi = -42;
        int minRssi = -62;
        int span = maxRssi - minRssi;
        int temp_rssi = model.rssi;

        if (temp_rssi > maxRssi) {
            temp_rssi = maxRssi;
        }else if (temp_rssi < minRssi) {
            temp_rssi = minRssi;
        }
        int iconIndex = (int) ((double) (4 * (temp_rssi - minRssi)) / (span));
        switch (iconIndex) {
            case 0:
                imgRssi.setImageResource(R.drawable.ic_signal_wifi_0_bar_24dp);
                break;
            case 1:
                imgRssi.setImageResource(R.drawable.ic_signal_wifi_1_bar_24dp);
                break;
            case 2:
                imgRssi.setImageResource(R.drawable.ic_signal_wifi_2_bar_24dp);
                break;
            case 3:
                imgRssi.setImageResource(R.drawable.ic_signal_wifi_3_bar_24dp);
                break;
            case 4:
                imgRssi.setImageResource(R.drawable.ic_signal_wifi_4_bar_24dp);
                break;
            default:
                imgRssi.setImageResource(R.drawable.ic_signal_wifi_off_24dp);
                break;
        }
        txtRssi.setText(model.rssi + " dBm");
    }

    public void setBatteryIcon() {
        double battery_voltage = ((double)brobot.batteryLevel / 1024) * 1.2 * 3 *(Brobot.BATTERY_RESISTOR_HIGH + Brobot.BATTERY_RESISTOR_LOW) / Brobot.BATTERY_RESISTOR_LOW;
        double maxBatteryLevel = 7.6;
        double minBatteryLevel = 6.8;
        double span = maxBatteryLevel - minBatteryLevel;


        if (battery_voltage > maxBatteryLevel) {
            battery_voltage = maxBatteryLevel;
        }else if (battery_voltage < minBatteryLevel) {
            battery_voltage = minBatteryLevel;
        }

        int batteryPercentage = (int) (100 * ((battery_voltage - minBatteryLevel) / span));

        if ((batteryPercentage < 20)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_empty_24dp);
        }else if ((batteryPercentage >= 20) && (batteryPercentage < 30)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_20_24dp);
        }else if ((batteryPercentage >= 30) && (batteryPercentage < 50)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_30_24dp);
        }else if ((batteryPercentage >= 50) && (batteryPercentage < 60)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_50_24dp);
        }else if ((batteryPercentage >= 60) && (batteryPercentage < 80)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_60_24dp);
        }else if ((batteryPercentage >= 80) && (batteryPercentage < 90)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_80_24dp);
        }else if ((batteryPercentage >= 90) && (batteryPercentage < 95)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_90_24dp);
        }else if ((batteryPercentage >= 95) && (batteryPercentage <= 100)) {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_full_24dp);
        }else {
            imgBatteryLevel.setImageResource(R.drawable.ic_battery_unknown_24dp);
        }
        txtBatteryLevel.setText(batteryPercentage + " %");
        txtBatteryVoltage.setText(String.format("%.2fV", battery_voltage));
    }

    public void setCurrentGauge(byte[] currents) {
        currentGauge0.setValue(currents[0]);
        txtCurrent0.setText(String.format("%.02f A",currents[0]*0.150));
        currentGauge1.setValue(currents[1]);
        txtCurrent1.setText(String.format("%.02f A",currents[1]*0.150));
    }

    private void startSpeedTimer() {

        timerSpeedUpdate = new Timer();
        timerSpeedUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (brobot.qikMotorControl != null) {
                            setBrobotSpeed(joystick.getSpeedX(), joystick.getSpeedY());
                        }
                    }
                });
            }
        }, 0, 100);
    }

    public void setBrobotLayoutValuesWhenConnected() {
        btnConnect.setEnabled(false);
        btnDisconnect.setEnabled(true);
        imgBrobot.setImageResource(R.drawable.brobot_red);
        model.bluetoothGatt.readRemoteRssi();
        setSignalStrengthIcon();
        imgRssi.setImageAlpha(0xFF);
        setBatteryIcon();
        imgBatteryLevel.setImageAlpha(0xFF);
        startSpeedTimer();
    }

    public void setBrobotLayoutValuesWhenDisconnected() {
        if (timerSpeedUpdate != null) {
            timerSpeedUpdate.cancel();
        }
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        imgBrobot.setImageResource(R.drawable.brobot_gray);
        setSignalStrengthIcon();
        imgRssi.setImageResource(R.drawable.ic_signal_wifi_off_24dp);
        imgRssi.setImageAlpha(0x55);
        txtRssi.setText("--");
        imgBatteryLevel.setImageResource(R.drawable.ic_battery_unknown_24dp);
        imgBatteryLevel.setImageAlpha(0x55);
        txtBatteryVoltage.setText("--");
        txtBatteryLevel.setText("--");
        txtCurrent0.setText("--");
        txtCurrent1.setText("--");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                model.bluetoothScanner.ScanForDevices();
                break;
            case R.id.btn_disconnect:
                model.bluetoothCommunicationHandler.bluetoothDisconnect();
                setBrobotLayoutValuesWhenDisconnected();
                break;
            case R.id.btn_test1:
                //brobot.qikMotorControl.setSpeed(50, 50);
                //model.bluetoothCommunicationHandler.WriteSpeedCharacteristic(brobot.qikMotorControl.getSpeed());
                break;
            case R.id.btn_test2:
                brobot.qikMotorControl.setSpeed(0, 0);
                model.bluetoothCommunicationHandler.WriteSpeedCharacteristic(brobot.qikMotorControl.getSpeed());
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (model.bluetoothCurrentDevice != null) {
            setBrobotLayoutValuesWhenConnected();
        } else {
            setBrobotLayoutValuesWhenDisconnected();
        }
    }

    public void setBrobotSpeed(int x, int y) {
        coordinates.setText(x + " X , " + y + " Y");

        int speed0, speed1;
        int maxSpeed = BluetoothModel.BROBOT_MAX_SPEED;

        if (y >= 0) {
            speed0 = y + x;
            if (speed0 > maxSpeed) {
                speed0 = maxSpeed;
            } else if (speed0 < -maxSpeed) {
                speed0 = -maxSpeed;
            }

            speed1 = y - x;
            if (speed1 > maxSpeed) {
                speed1 = maxSpeed;
            } else if (speed1 < -maxSpeed) {
                speed1 = -maxSpeed;
            }
        } else {
            speed0 = y - x;
            if (speed0 > maxSpeed) {
                speed0 = maxSpeed;
            } else if (speed0 < -maxSpeed) {
                speed0 = -maxSpeed;
            }

            speed1 = y + x;
            if (speed1 > maxSpeed) {
                speed1 = maxSpeed;
            } else if (speed1 < -maxSpeed) {
                speed1 = -maxSpeed;
            }
        }

        brobot.qikMotorControl.setSpeed(speed1, speed0);
        model.bluetoothCommunicationHandler.WriteSpeedCharacteristic(brobot.qikMotorControl.getSpeed());

    }


}
