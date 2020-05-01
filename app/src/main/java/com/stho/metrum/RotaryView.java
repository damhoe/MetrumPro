package com.stho.metrum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.jetbrains.annotations.Nullable;

public class RotaryView extends android.support.v7.widget.AppCompatImageView {

    public float angle = 45;
    public float value;
    Paint paint;
    INotifyListener listener;
    Matrix matrix = new Matrix();

    public RotaryView(Context context) {
        super(context);
    }

    public RotaryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    void setNotifyListener(INotifyListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onCreate();
        rotateImage();
    }

    private void rotateImage() {
        int w = this.getDrawable().getIntrinsicWidth();
        int h = this.getDrawable().getIntrinsicHeight();
        matrix.reset();
        matrix.setRotate(angle, w / 2, h / 2);
        this.setScaleType(ScaleType.MATRIX);
        this.setImageMatrix(matrix);
    }

    private void onCreate() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        setImageResource(R.drawable.knob);
    }


    private double previousAlpha;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        float x = event.getX();
        float y = event.getY();
        double alpha = Math.atan2(y - cy, x - cx) * 180 / Math.PI;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                double delta = (alpha - previousAlpha);

                if (delta > 180)
                    delta -= 360;

                if (delta < -180)
                    delta += 360;

                angle += delta;

                if (listener != null)
                    listener.onNotifyAngle(delta);

                invalidate();
                break;

            case MotionEvent.ACTION_SCROLL:
                return false;
        }

        previousAlpha = alpha;
        return true;
    }
}
