package com.example.martin.brobotgui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Martin on 27.02.2016.
 */
public class SetupFragment extends Fragment implements View.OnClickListener{

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

    MainActivity activity;

    BluetoothModel model;
    Brobot brobot;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setup_layout, container, false);

        activity = ((MainActivity) getActivity());
        model = activity.getModel();
        createGui(view);
        brobot = activity.getBrobot();
        updateQikConfigGuiValues();
        return view;
    }

    private void createGui(View view) {
        txtDeviceIdValue = (TextView) view.findViewById(R.id.txt_device_id_value);
        txtPwmFreqValue = (TextView) view.findViewById(R.id.txt_pwm_freq_value);
        txtStopOnErrorValue = (TextView) view.findViewById(R.id.txt_stop_on_err_value);
        txtSerialTimeoutValue = (TextView) view.findViewById(R.id.txt_serial_timeout_value);
        txtAccelerationValue = (TextView) view.findViewById(R.id.txt_motor_acceleration_value);
        txtBreakDurValue = (TextView) view.findViewById(R.id.txt_motor_break_duration_value);
        txtCurrentLimValue = (TextView) view.findViewById(R.id.txt_motor_current_lim_value);
        txtCurrentLimRespValue = (TextView) view.findViewById(R.id.txt_motor_current_lim_resp_value);

        btnReadConfigs = (Button) view.findViewById(R.id.btn_read_cfg);
        btnReadConfigs.setOnClickListener(this);
        btnWriteConfigs = (Button) view.findViewById(R.id.btn_write_cfg);
        btnWriteConfigs.setOnClickListener(this);
        btnWriteDefaultValues = (Button) view.findViewById(R.id.btn_write_default_values);
        btnWriteDefaultValues.setOnClickListener(this);

        cbxAnyMotorErr = (CheckBox) view.findViewById(R.id.cbx_stop_on_any_motor_fault);
        cbxOverCurErr = (CheckBox) view.findViewById(R.id.cbx_stop_on_over_current);
        cbxSerialErr = (CheckBox) view.findViewById(R.id.cbx_stop_on_serial);

        spnPwmFrequency = (Spinner) view.findViewById(R.id.spn_pwm_frequency);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.pqm_frequency_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPwmFrequency.setAdapter(spinnerAdapter);

        // SERIAL TIMEOUT SEEKBAR
        skbSerialTimeout = (SeekBar) view.findViewById(R.id.skb_serial_timeout);
        txtSerialTimeoutSeekbar = (TextView) view.findViewById(R.id.txt_seekbar_serial_timeout);
        txtSerialTimeoutSeekbar.setText(String.format("%02d / %02d sec", (skbSerialTimeout.getProgress() * 2), (skbSerialTimeout.getMax() * 2)));
        skbSerialTimeout.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int serialTimeoutValue;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        serialTimeoutValue = progress;
                        txtSerialTimeoutSeekbar.setText(String.format("%02d / %02d sec", (serialTimeoutValue * 2), (skbSerialTimeout.getMax() * 2)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        txtSerialTimeoutSeekbar.setText(String.format("%02d / %02d sec", (serialTimeoutValue * 2), (skbSerialTimeout.getMax() * 2)));
                    }
                }
        );

        // MOTOR ACCELERATION SEEKBAR
        skbAcceleration = (SeekBar) view.findViewById(R.id.skb_motor_acceleration);
        txtAcceelerationSeekbar = (TextView) view.findViewById(R.id.txt_seekbar_motor_acceleration_value);
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
        skbBreakDuration = (SeekBar) view.findViewById(R.id.skb_motor_break_duration);
        txtBreakDurationSeekbar = (TextView) view.findViewById(R.id.txt_seekbar_motor_break_duration);
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
        skbCurrentLimit = (SeekBar) view.findViewById(R.id.skb_motor_current_limit);
        txtCurrentLimitSeekbar = (TextView) view.findViewById(R.id.txt_seekbar_motor_current_limit);
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
        skbCurrentLimitResponse = (SeekBar) view.findViewById(R.id.skb_motor_current_limit_response);
        txtCurrentLimitResponseSeekbar = (TextView) view.findViewById(R.id.txt_seekbar_motor_current_limit_response);
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


        if (model.bluetoothCurrentDevice != null) {
            btnReadConfigs.setEnabled(true);
            btnWriteConfigs.setEnabled(true);
            btnWriteDefaultValues.setEnabled(true);
        } else {
            btnReadConfigs.setEnabled(false);
            btnWriteConfigs.setEnabled(false);
            btnWriteDefaultValues.setEnabled(false);
        }

    }

    private void changeAdapterList(int index) {
        String[] newList = activity.getResources().getStringArray(R.array.pqm_frequency_array);
        String temp = newList[0];
        newList[0] = newList[index];
        newList[index] = temp;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, newList);
        spnPwmFrequency.setAdapter(adapter);
    }


    public void updateQikConfigGuiValues() {
        txtDeviceIdValue.setText(String.valueOf(brobot.qikMotorControl.getDeviceIdValue()));
        txtPwmFreqValue.setText(String.valueOf(brobot.qikMotorControl.getPwmFreqValue()));
        txtStopOnErrorValue.setText(String.valueOf(brobot.qikMotorControl.getStopOnErrorValue()));
        txtSerialTimeoutValue.setText(String.valueOf(brobot.qikMotorControl.getSerialTimeoutValue()));
        skbSerialTimeout.setProgress(brobot.qikMotorControl.getSerialTimeoutValue());
        txtAccelerationValue.setText(String.valueOf(brobot.qikMotorControl.getAcceleration()));
        skbAcceleration.setProgress(brobot.qikMotorControl.getAcceleration());
        txtBreakDurValue.setText(String.valueOf(brobot.qikMotorControl.getBreakDuration()));
        skbBreakDuration.setProgress(brobot.qikMotorControl.getBreakDuration());
        txtCurrentLimValue.setText(String.valueOf(brobot.qikMotorControl.getCurrentLimit()));
        skbCurrentLimit.setProgress(brobot.qikMotorControl.getCurrentLimit());
        txtCurrentLimRespValue.setText(String.valueOf(brobot.qikMotorControl.getCurrentLimitResponse()));
        skbCurrentLimitResponse.setProgress(brobot.qikMotorControl.getCurrentLimitResponse());

        if(brobot.qikMotorControl.getPwmFreqValue() == 2) {
            changeAdapterList(1);
        }else if(brobot.qikMotorControl.getPwmFreqValue() == 4) {
            changeAdapterList(2);
        }

        cbxAnyMotorErr.setChecked(((brobot.qikMotorControl.getStopOnErrorValue() & 0x04) > 0));
        cbxOverCurErr.setChecked(((brobot.qikMotorControl.getStopOnErrorValue() & 0x02) > 0));
        cbxSerialErr.setChecked(((brobot.qikMotorControl.getStopOnErrorValue() & 0x01) > 0));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_cfg:
                model.bluetoothCommunicationHandler.readConfigCharacteristic();
                updateQikConfigGuiValues();
                break;
            case R.id.btn_write_cfg:
                EncodeQikCfgValues();
                break;
            case R.id.btn_write_default_values:
                brobot.qikMotorControl.setDefaultValues();
                model.bluetoothCommunicationHandler.WriteConfigCharacteristic(brobot.qikMotorControl);
                break;
            case R.id.spn_pwm_frequency:
//                Log.i(MY_TAG, "SPn tucjh");
                break;
            default:
                break;
        }
    }

    public void EncodeQikCfgValues() {
        QikMotorControl qikMotorControl = new QikMotorControl(activity);
        qikMotorControl.setDeviceIdValue(10);

        qikMotorControl.setSerialTimeoutValue(0); // 0 for safety reasons

        byte pwmFrequency = 0;
        if (spnPwmFrequency.getSelectedItem().equals(getString(R.string.pwm_freq_19_7khz))) {
            pwmFrequency = 0;
        }else if (spnPwmFrequency.getSelectedItem().equals(getString(R.string.pwm_freq_2_5khz))) {
            pwmFrequency = 2;
        }else if (spnPwmFrequency.getSelectedItem().equals(getString(R.string.pwm_freq_310hz))) {
            pwmFrequency = 4;
        }else
            Log.i("dfaf", "Spinner didn't funk" + R.string.pwm_freq_19_7khz);
        qikMotorControl.setPwmFreqValue(pwmFrequency);


        byte setStopOnErrorValue = 0;
        if (cbxSerialErr.isChecked()) {
            setStopOnErrorValue |= 0x00; // FAIL SAFE! TURNING OFF SERIAL TIMEOUT
        }
        if (cbxOverCurErr.isChecked()) {
            setStopOnErrorValue |= 0x02;
        }
        if (cbxAnyMotorErr.isChecked()) {
            setStopOnErrorValue |= 0x04;
        }
        qikMotorControl.setStopOnErrorValue(setStopOnErrorValue);

        qikMotorControl.setSerialTimeoutValue(0);

        qikMotorControl.setAcceleration(skbAcceleration.getProgress());
        qikMotorControl.setBreakDuration(skbBreakDuration.getProgress());
        qikMotorControl.setCurrentLimit(skbCurrentLimit.getProgress());
        qikMotorControl.setCurrentLimitResp(skbCurrentLimitResponse.getProgress());

        model.bluetoothCommunicationHandler.WriteConfigCharacteristic(qikMotorControl);
    }

}
