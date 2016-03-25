package com.example.martin.brobotgui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Martin on 01.03.2016.
 */
public class CurrentGauge extends View {

    private RectF rectangle;
    private Paint paint;
    private float strokeWidth;
    private int strokeColor;
    private float startAngle;
    private float stopAngle;
    private float sweepAngle;
    private float rectLeft;
    private float rectRight;
    private float rectTop;
    private float rectBottom;
    private int padding;
    private int pointEndColor;
    private int pointStartColor;
    private int pointSize;
    private int point;
    private float pointAngle = 30;
    private int value;
    private int startValue;
    private int endValue;
    private LinearGradient pointGradient;


    public CurrentGauge(Context context) {
        super(context);
        init();
    }

    public CurrentGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CurrentGauge, 0, 0);
        strokeColor = a.getColor(R.styleable.CurrentGauge_strokeColor, getResources().getColor(R.color.brobot_black));
        strokeWidth = a.getDimension(R.styleable.CurrentGauge_strokeWidth, 20);
        startAngle = a.getDimension(R.styleable.CurrentGauge_startAngle, 120);
        stopAngle = a.getDimension(R.styleable.CurrentGauge_stopAngle, 300);
        sweepAngle = a.getInt(R.styleable.CurrentGauge_sweepAngle, 300);
        padding = a.getInteger(R.styleable.CurrentGauge_padding, (int) (strokeWidth / 2));
        pointEndColor = a.getColor(R.styleable.CurrentGauge_pointEndColor, getResources().getColor(R.color.brobot_black));
        pointStartColor = a.getColor(R.styleable.CurrentGauge_pointStartColor, getResources().getColor(R.color.palette_red_darkest));
        pointSize = a.getInteger(R.styleable.CurrentGauge_pointSize, 0);
        startValue = a.getInteger(R.styleable.CurrentGauge_startValue, 0);
        endValue = a.getInteger(R.styleable.CurrentGauge_endValue, 40);

        // calculating one point sweep
        pointAngle = (Math.abs(sweepAngle) / (endValue - startValue));

        a.recycle();
        init();
    }

    void init() {
        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(strokeColor);
        paint.setAlpha(0x55);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        pointGradient = new LinearGradient(getWidth(), getHeight(), 0, 0, pointStartColor, pointEndColor, Shader.TileMode.CLAMP);

        rectangle = new RectF();

        value = startValue;
        point = (int)startAngle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPadding(padding, padding, padding, padding);
        float width = getWidth() - (padding+padding);
        float height = getHeight() - (padding+padding);
        float radius = (width > height ? width/2 : height/2);

        rectLeft = width/2 - radius + padding;
        rectTop = height/2 - radius + padding;
        rectRight = width/2 - radius + padding + width;
        rectBottom = height/2 - radius + padding + height;

        rectangle.set(rectLeft, rectTop, rectRight, rectBottom);

        paint.setColor(strokeColor);
        paint.setAlpha(0x55);
        paint.setShader(null);
        canvas.drawArc(rectangle, startAngle, sweepAngle, false, paint);

        paint.setColor(pointStartColor);
        paint.setAlpha(0xFF);
        paint.setShader(pointGradient);
        if (pointSize>0) {//if size of pointer is defined
            if (point > startAngle + pointSize/2) {
                canvas.drawArc(rectangle, point - pointSize/2, pointSize, false, paint);
            }
            else { //to avoid excedding start/zero point
                canvas.drawArc(rectangle, point, pointSize, false, paint);
            }
        }else { //draw from start point to value point (long pointer)
            if (value == startValue) { //use non-zero default value for start point (to avoid lack of pointer for start/zero value)
                canvas.drawArc(rectangle, startAngle, 1, false, paint);
            } else {
                canvas.drawArc(rectangle, startAngle, point - startAngle, false, paint);
            }
        }
    }

    public void setValue(int current) {
        value = (int)((float)(value * 0.5) + (float)(current * 0.5));
        point = (int) (startAngle + (value - startValue) * pointAngle);
        invalidate();
    }

}
