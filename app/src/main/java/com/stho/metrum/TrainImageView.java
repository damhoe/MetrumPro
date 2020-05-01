package com.stho.metrum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class TrainImageView extends android.support.v7.widget.AppCompatImageView {

    private float x = 0;
    private int width;
    private int height;
    private int xPos;
    private float direction = 1;// 1 means moving to the right display edge
    private Bitmap unscaledBmpLeftLookingTrain;
    private Bitmap unscaledBmpRightLookingTrain;

    public TrainImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = false;
        unscaledBmpLeftLookingTrain = BitmapFactory.decodeResource(context.getResources(), R.drawable.train, bitmapOptions);
        unscaledBmpRightLookingTrain = BitmapFactory.decodeResource(context.getResources(), R.drawable.train_right, bitmapOptions);

        xPos = 0;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        float scale = 0.8f;

        Bitmap bmp;
        if (direction == 1) {
            float ratio = (float) height / unscaledBmpRightLookingTrain.getHeight();
            float newWidth = ratio * unscaledBmpRightLookingTrain.getWidth();
            bmp = (Bitmap) Bitmap.createScaledBitmap(unscaledBmpRightLookingTrain, (int) (newWidth * scale), (int) (height * scale), false);
        } else {
            float ratio = (float) height / unscaledBmpLeftLookingTrain.getHeight();
            float newWidth = ratio * unscaledBmpLeftLookingTrain.getWidth();
            bmp = (Bitmap) Bitmap.createScaledBitmap(unscaledBmpLeftLookingTrain, (int) (newWidth * scale), (int) (height * scale),false);
        }


        float dx = x * (width - bmp.getWidth()); //(bmp.getWidth()) * 0.5f;
        float dy = (height - bmp.getHeight()) * 0.5f;

        canvas.drawBitmap(bmp, dx, dy, new Paint());
    }

    public void moveX(float x, float direction){
        this.x = x;
        this.direction = direction;
        invalidate();
    }
}
