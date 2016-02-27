package com.example.martin.brobotgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Martin on 21.02.2016.
 */
public class SetupActivity extends Activity implements View.OnClickListener{

    TextView txtDeviceIdValue;
    TextView txtPwmFreqValue;
    TextView txtStopOnErrorValue;
    TextView txtSerialTimeoutValue;
    TextView txtAccelerationValue;
    TextView txtBreakDurValue;
    TextView txtCurrentLimValue;
    TextView txtCurrentLimRespValue;
    Button btnReadConfigs;
    Button btnWriteConfigs;
    Button btnWriteDefaultValues;

    CheckBox cbxSerialErr;
    CheckBox cbxOverCurErr;
    CheckBox cbxAnyMotorErr;

    Spinner spnPwmFrequency;

    SeekBar skbSerialTimeout;
    TextView txtSerialTimeoutSeekbar;

    SeekBar skbAcceleration;
    TextView txtAcceelerationSeekbar;

    SeekBar skbBreakDuration;
    TextView txtBreakDurationSeekbar;

    SeekBar skbCurrentLimit;
    TextView txtCurrentLimitSeekbar;

    SeekBar skbCurrentLimitResponse;
    TextView txtCurrentLimitResponseSeekbar;


    private Handler speedHandler;

//    private BluetoothModel model;

    public final String MY_TAG = "Qik";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        setGuiComponents();



//        model.bluetoothCommunicationHandler.startCommunication();
    }

    private void setGuiComponents() {
        txtDeviceIdValue = (TextView) findViewById(R.id.txt_device_id_value);
        txtPwmFreqValue = (TextView) findViewById(R.id.txt_pwm_freq_value);
        txtStopOnErrorValue = (TextView) findViewById(R.id.txt_stop_on_err_value);
        txtSerialTimeoutValue = (TextView) findViewById(R.id.txt_serial_timeout_value);
        txtAccelerationValue = (TextView) findViewById(R.id.txt_motor_acceleration_value);
        txtBreakDurValue = (TextView) findViewById(R.id.txt_motor_break_duration_value);
        txtCurrentLimValue = (TextView) findViewById(R.id.txt_motor_current_lim_value);
        txtCurrentLimRespValue = (TextView) findViewById(R.id.txt_motor_current_lim_resp_value);

        btnReadConfigs = (Button) findViewById(R.id.btn_read_cfg);
        btnReadConfigs.setOnClickListener(this);
        btnWriteConfigs = (Button) findViewById(R.id.btn_write_cfg);
        btnWriteConfigs.setOnClickListener(this);
        btnWriteDefaultValues = (Button) findViewById(R.id.btn_write_default_values);
        btnWriteDefaultValues.setOnClickListener(this);

        cbxAnyMotorErr = (CheckBox) findViewById(R.id.cbx_stop_on_any_motor_fault);
        cbxOverCurErr = (CheckBox) findViewById(R.id.cbx_stop_on_over_current);
        cbxSerialErr = (CheckBox) findViewById(R.id.cbx_stop_on_serial);

        spnPwmFrequency = (Spinner) findViewById(R.id.spn_pwm_frequency);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.pqm_frequency_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPwmFrequency.setAdapter(spinnerAdapter);

        // SERIAL TIMEOUT SEEKBAR
        skbSerialTimeout = (SeekBar) findViewById(R.id.skb_serial_timeout);
        txtSerialTimeoutSeekbar = (TextView) findViewById(R.id.txt_seekbar_serial_timeout);
        txtSerialTimeoutSeekbar.setText(String.format("%02d / %02d sec", (skbSerialTimeout.getProgress()*2), (skbSerialTimeout.getMax() * 2)));
        skbSerialTimeout.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int serialTimeoutValue;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        serialTimeoutValue = progress;
                        txtSerialTimeoutSeekbar.setText(String.format("%02d / %02d sec", (serialTimeoutValue * 2), (skbSerialTimeout.getMax() * 2)));

//                        model.qikConfig.getSerialTimeoutValuePrototype(serialTimeoutValue);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtSerialTimeoutSeekbar.setText(String.format("%02d / %02d sec", (serialTimeoutValue * 2), (skbSerialTimeout.getMax() * 2)));
//                        model.qikConfig.getSerialTimeoutValuePrototype(serialTimeoutValue);
                    }
                }
        );

        // MOTOR ACCELERATION SEEKBAR
        skbAcceleration = (SeekBar) findViewById(R.id.skb_motor_acceleration);
        txtAcceelerationSeekbar = (TextView) findViewById(R.id.txt_seekbar_motor_acceleration_value);
        txtAcceelerationSeekbar.setText(skbAcceleration.getProgress() + " / " + skbAcceleration.getMax());
        skbAcceleration.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int accelerationValue;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        accelerationValue = progress;
                        txtAcceelerationSeekbar.setText(accelerationValue + " / " + skbAcceleration.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtAcceelerationSeekbar.setText(accelerationValue + " / " + skbAcceleration.getMax());
                    }
                }
        );

        // MOTOR BREAK DURATION SEEKBAR
        skbBreakDuration = (SeekBar) findViewById(R.id.skb_motor_break_duration);
        txtBreakDurationSeekbar = (TextView) findViewById(R.id.txt_seekbar_motor_break_duration);
        txtBreakDurationSeekbar.setText(skbBreakDuration.getProgress() + " / " + skbBreakDuration.getMax());
        skbBreakDuration.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int breakDurationValue;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        breakDurationValue = progress;
                        txtBreakDurationSeekbar.setText(breakDurationValue + " / " + skbBreakDuration.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtBreakDurationSeekbar.setText(breakDurationValue + " / " + skbBreakDuration.getMax());
                    }
                }
        );

        // MOTOR CURRENT LIMIT SEEKBAR
        skbCurrentLimit = (SeekBar) findViewById(R.id.skb_motor_current_limit);
        txtCurrentLimitSeekbar = (TextView) findViewById(R.id.txt_seekbar_motor_current_limit);
        txtCurrentLimitSeekbar.setText(skbCurrentLimit.getProgress() + " / " + skbCurrentLimit.getMax());
        skbCurrentLimit.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int currentLimitValue;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        currentLimitValue = progress;
                        txtCurrentLimitSeekbar.setText(currentLimitValue + " / " + skbCurrentLimit.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtCurrentLimitSeekbar.setText(currentLimitValue + " / " + skbCurrentLimit.getMax());
                    }
                }
        );

        // MOTOR CURRENT LIMIT RESPONSE SEEKBAR
        skbCurrentLimitResponse = (SeekBar) findViewById(R.id.skb_motor_current_limit_response);
        txtCurrentLimitResponseSeekbar = (TextView) findViewById(R.id.txt_seekbar_motor_current_limit_response);
        txtCurrentLimitResponseSeekbar.setText(skbCurrentLimitResponse.getProgress() + " / " + skbCurrentLimitResponse.getMax());
        skbCurrentLimitResponse.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int currentLimitResponseValue;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        currentLimitResponseValue = progress;
                        txtCurrentLimitResponseSeekbar.setText(currentLimitResponseValue + " / " + skbCurrentLimitResponse.getMax());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtCurrentLimitResponseSeekbar.setText(currentLimitResponseValue + " / " + skbCurrentLimitResponse.getMax());
                    }
                }
        );


    }


//    public void updateQikConfigGuiValues() {
//        txtDeviceIdValue.setText("" + model.qikConfig.getDeviceIdValue());
//        txtPwmFreqValue.setText("" + model.qikConfig.getPwmFreqValue());
//        txtStopOnErrorValue.setText("" + model.qikConfig.getStopOnErrorValue());
//        txtSerialTimeoutValue.setText("" + model.qikConfig.getSerialTimeoutValue());
//        txtAccelerationValue.setText("" + model.qikConfig.getAcceleration());
//        txtBreakDurValue.setText("" + model.qikConfig.getBreakDuration());
//        txtCurrentLimValue.setText("" + model.qikConfig.getCurrentLimit());
//        txtCurrentLimRespValue.setText("" + model.qikConfig.getCurrentLimitResponse());
//    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_disconnect:
//                model.bluetoothCommunicationHandler.bluetoothDisconnect();
//                disconnectMainActivity();
//                break;
//            case R.id.btn_scan:
//                model.bluetoothScanner.ScanForDevices();
//                break;
//            case R.id.btn_read_cfg:
//                model.bluetoothCommunicationHandler.readConfigCharacteristic();
//                break;
//            case R.id.btn_write_cfg:
//                EncodeQikCfgValues();
//                break;
//            case R.id.btn_write_default_values:
//                model.qikConfig.setDefaultValues();
//                model.bluetoothCommunicationHandler.WriteConfigCharacteristic(model.qikConfig);
//                break;
//            case R.id.spn_pwm_frequency:
//                Log.i(MY_TAG, "SPn tucjh");
//                break;
//            default:
//                break;
//        }
    }
//
//    public void EncodeQikCfgValues() {
//        QikConfig newQikConfig = new QikConfig(this);
//        newQikConfig.setDeviceIdValue(10);
//
//        newQikConfig.setSerialTimeoutValue(0); // 0 for safety reasons
//
//        byte pwmFrequency = 0;
//        if (spnPwmFrequency.getSelectedItem().equals(getString(R.string.pwm_freq_19_7khz))) {
//            pwmFrequency = 0;
//        }else if (spnPwmFrequency.getSelectedItem().equals(getString(R.string.pwm_freq_2_5khz))) {
//            pwmFrequency = 2;
//        }else if (spnPwmFrequency.getSelectedItem().equals(getString(R.string.pwm_freq_310hz))) {
//            pwmFrequency = 4;
//        }else
//            Log.i(MY_TAG, "Spinner didn't funk" + R.string.pwm_freq_19_7khz);
//        newQikConfig.setPwmFreqValue(pwmFrequency);
//
//
//        byte setStopOnErrorValue = 0;
//        if (cbxSerialErr.isChecked()) {
//            setStopOnErrorValue |= 0x00; // FAIL SAFE! TURNING OFF SERIAL TIMEOUT
//        }
//        if (cbxOverCurErr.isChecked()) {
//            setStopOnErrorValue |= 0x02;
//        }
//        if (cbxAnyMotorErr.isChecked()) {
//            setStopOnErrorValue |= 0x04;
//        }
//        newQikConfig.setStopOnErrorValue(setStopOnErrorValue);
//
//        newQikConfig.setSerialTimeoutValue(0);
//
//        newQikConfig.setAcceleration(skbAcceleration.getProgress());
//        newQikConfig.setBreakDuration(skbBreakDuration.getProgress());
//        newQikConfig.setCurrentLimit(skbCurrentLimit.getProgress());
//        newQikConfig.setCurrentLimitResp(skbCurrentLimitResponse.getProgress());
//
//        model.bluetoothCommunicationHandler.WriteConfigCharacteristic(newQikConfig);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
