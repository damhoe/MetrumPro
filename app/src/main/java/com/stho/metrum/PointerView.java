package com.stho.metrum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.Nullable;

import static com.stho.metrum.MainActivity.POINTER_VIEW_KEY;
import static com.stho.metrum.ViewModel.DEFAULT_BPM;

public class PointerView extends RelativeLayout implements View.OnTouchListener {

    private RelativeLayout knobView;
    private INotifyListener listener;

    private int yMiddle;
    private int hKnob;
    private int yMin;
    private int yMax;
    private int knobMargin = 39;
    private int previousX;
    private int previousY;
    private int minTempo = 40;
    private int maxTempo = 208;
    private int tempoResulution = 2; // bpm

    public PointerView(@NonNull Context context) {
        super(context);
        init();
    }
    public PointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void setNotifyListener(INotifyListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        inflate(getContext(), R.layout.pointer_view, this);
        this.setOnTouchListener(this);
        knobView = (RelativeLayout) findViewById(R.id.pointer_knob);
        yMin = knobMargin;
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hKnob = knobView.getHeight();
                yMiddle = (int) getHeight() / 2;
                yMax = yMiddle - knobMargin;
                setStartTempo();
            }
        });
    }

    public void setStartTempo() {
        moveKnob(getY(DEFAULT_BPM));
    }

    private boolean onKnob(int x, int y) {
        int xStart = (int) 50;
        int yStart = (int) knobView.getY();
        int xEnd = xStart + 180;
        int yEnd = yStart + 100;

        return xStart < x && x < xEnd && yStart < y && y < yEnd;
    }

    private void moveKnob(int y) {
        int newY = y - hKnob / 2;
        if (yMin > newY) {
            knobView.setY(yMin);
        }
        else if (newY > yMax) {
            knobView.setY(yMax);
        }
        else knobView.setY(newY);
    }

    private int getY(int tempo) {
        float deltaY = (yMax - yMin) * tempoResulution / (maxTempo - minTempo);
        return (int) (tempo * deltaY /tempoResulution);
    }

    private int getTempo(int y) {
        float deltaY = (yMax - yMin) * tempoResulution / (maxTempo - minTempo);
        return (int) (y * tempoResulution / deltaY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean isMovingKnob = false;

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isMovingKnob = true;
                if (listener != null) {
                    listener.onNotifyMotionEvent(POINTER_VIEW_KEY, event);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                int dx = x - previousX;
                int dy = y - previousY;

                if (onKnob(x, y)) {
                    if (dy * dy > dx * dx) {
                        isMovingKnob = true;
                        moveKnob(y);

                        listener.onNotifyTempoChanging(getTempo(y));
                        // TODO change tempo
                    }
                }
                else {
                    isMovingKnob = false;
                    if (listener != null) {
                        listener.onNotifyMotionEvent(POINTER_VIEW_KEY, event);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                isMovingKnob = false;
                if (listener != null) {
                    listener.onNotifyMotionEvent(POINTER_VIEW_KEY, event);
                }
                break;
            }
        }
        previousX = x;
        previousY = y;

        return isMovingKnob;
    }
}
