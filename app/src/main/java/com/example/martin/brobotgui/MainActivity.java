package com.example.martin.brobotgui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity{

    public final String LOG_TAG = "Brobot";
    BluetoothModel model;
    Brobot brobot;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBrobot();
        setModel();
        initFragment();
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

    public void connectMainActivity() {
        Fragment fragment = fragmentManager.findFragmentByTag("MAINFRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            ((MainFragment) fragment).setBrobotLayoutValuesWhenConnected();
        }
    }

    public void disconnectMainActivity() {
        Fragment fragment = fragmentManager.findFragmentByTag("MAINFRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            ((MainFragment) fragment).setBrobotLayoutValuesWhenDisconnected();
        }
    }

    public void setSignalStrengthIconMainActivity() {
        Fragment fragment = fragmentManager.findFragmentByTag("MAINFRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            ((MainFragment) fragment).setSignalStrengthIcon();
        }
    }

    public void setBatteryVoltageMainActivity() {
        Fragment fragment = fragmentManager.findFragmentByTag("MAINFRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            ((MainFragment) fragment).setBatteryIcon();
        }
    }


    public void initFragment() {
        fragmentManager = getFragmentManager();
        changeFragment(new MainFragment(), "MAINFRAGMENT");

    }

    public void changeFragment(Fragment fragment, String tag){

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void updateSetupFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag("SETUPFRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            ((SetupFragment) fragment).updateQikConfigGuiValues();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean checkIfSetupFragmentIdVisible() {
        Fragment fragment = fragmentManager.findFragmentByTag("SETUPFRAGMENT");
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (  !checkIfSetupFragmentIdVisible()) {
                changeFragment(new SetupFragment(), "SETUPFRAGMENT");
                return true;
            }
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
