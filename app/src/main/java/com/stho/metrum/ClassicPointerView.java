package com.stho.metrum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import org.jetbrains.annotations.Nullable;

public class ClassicPointerView extends android.support.v7.widget.AppCompatImageView {

    public float angle = 0;
    public float value;
    Paint paint;
    Matrix matrix = new Matrix();

    public ClassicPointerView(Context context) {
        super(context);
    }

    public ClassicPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

 /*   void setNotifyListener(INotifyListener listener) {
        this.listener = listener;
    }
*/
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
        setImageResource(R.drawable.classic_pointer);
    }
}
