package com.example.martin.brobotgui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Joystick extends LinearLayout {

    //public static int brobotSpeed = 127;
    public static int brobotSpeed;

    private Context context;

    private TextView textView;
    private MainFragment mainFragment;

    private int joystickWidth;
    private int joystickHeight;

    private int speedX, speedY;

    public Joystick(Context context, AttributeSet attr) {
        super(context, attr);
        inflate();
        this.context = context;
        addTouchListeners();
        brobotSpeed = BluetoothModel.BROBOT_MAX_SPEED;
    }

    private void addTouchListeners() {
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    joystickWidth = getWidth() / 2;
                    joystickHeight = getHeight() / 2;

                    int x = (int) (event.getX() - (joystickWidth));
                    int y = (int) (event.getY() - (joystickHeight));

                    updateSpeed(getX(x, joystickWidth), getY(y, joystickHeight));
                }else if (event.getAction() == MotionEvent.ACTION_UP) {
                    updateSpeed(0,0);
                }
                return true;
            }
        });
    }

    private int getX(int x, int joystickWidth){

        int outOfBoundsX = 0;

        if(x > joystickWidth){
            outOfBoundsX = (x - joystickWidth);
        }
        if(x < (-joystickWidth)){
            outOfBoundsX = -((-x) - joystickWidth);
        }
        return translateToBrobotSpeed(x-outOfBoundsX);
    }

    private int getY(int y, int joystickHeight){

        int outOfBoudsY = 0;

        if(y > joystickHeight){
            outOfBoudsY = (y - joystickHeight);
        }
        if(y < -(joystickHeight)){
            outOfBoudsY = -((-y) - joystickHeight);
        }
        return translateToBrobotSpeed(-(y-outOfBoudsY));
    }

    private void inflate(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.joystick, this, true);
    }

    private int translateToBrobotSpeed(int i){
        double percentOfMeasuredValue = (i*100)/joystickHeight;
        return (int) ((percentOfMeasuredValue/100) * brobotSpeed);
    }

    public void setMainFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    private void updateSpeed(int x, int y){
        mainFragment.setBrobotSpeed(x, y);
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }
}
