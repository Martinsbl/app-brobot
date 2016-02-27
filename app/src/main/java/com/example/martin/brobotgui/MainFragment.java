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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        activity = ((MainActivity) getActivity());
        createGui(view);

        model = activity.getModel();
        brobot = activity.getBrobot();
        model.bluetoothCommunicationHandler.startCommunication();

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

    public void setBrobotLayoutValuesWhenConnected() {
        btnConnect.setEnabled(false);
        btnDisconnect.setEnabled(true);
        imgBrobot.setImageResource(R.drawable.brobot_red);
        model.bluetoothGatt.readRemoteRssi();
        setSignalStrengthIcon();
        imgRssi.setImageAlpha(0xFF);
    }

    public void setBrobotLayoutValuesWhenDisconnected() {
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        imgBrobot.setImageResource(R.drawable.brobot_gray);
        setSignalStrengthIcon();
        imgRssi.setImageResource(R.drawable.ic_signal_wifi_off_24dp);
        imgRssi.setImageAlpha(0x55);
        txtRssi.setText(" ");
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
                brobot.qikMotorControl.setSpeed(50, 50);
                model.bluetoothCommunicationHandler.WriteSpeedCharacteristic(brobot.qikMotorControl.getSpeed());
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
}
