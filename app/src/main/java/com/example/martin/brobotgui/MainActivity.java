package com.example.martin.brobotgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener{

    public final String LOG_TAG = "Brobot";
    Button btnConnect;
    Button btnDisconnect;
    Button btnTest1;
    Button btnTest2;
    ImageView imgBrobot;
    BluetoothModel model;
    Brobot brobot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGui();
        setBrobot();
        setModel();
        model.bluetoothCommunicationHandler.startCommunication();
    }

    public Brobot getBrobot() {
        return brobot;
    }

    public void setBrobot() {
        brobot = new Brobot();
    }

    public BluetoothModel getModel() {
        return model;
    }

    public void setModel() {
        model = new BluetoothModel();
        model.bluetoothCommunicationHandler = new BluetoothCommunicationHandler(this);
    }


    private void createGui() {
        btnConnect = (Button) findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);
        btnDisconnect = (Button) findViewById(R.id.btn_disconnect);
        btnDisconnect.setOnClickListener(this);
        imgBrobot = (ImageView) findViewById(R.id.img_brobot);

        btnTest1 = (Button) findViewById(R.id.btn_test1);
        btnTest1.setOnClickListener(this);
        btnTest2 = (Button) findViewById(R.id.btn_test2);
        btnTest2.setOnClickListener(this);
    }

    public void connectMainActivity() {
        btnConnect.setEnabled(false);
        btnDisconnect.setEnabled(true);
        imgBrobot.setImageResource(R.drawable.brobot_red);
    }

    public void disconnectMainActivity() {
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        imgBrobot.setImageResource(R.drawable.brobot_gray);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                model.bluetoothScanner.ScanForDevices();
                break;
            case R.id.btn_disconnect:
                model.bluetoothCommunicationHandler.bluetoothDisconnect();
                disconnectMainActivity();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SetupActivity.class);
            startActivity(openSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
