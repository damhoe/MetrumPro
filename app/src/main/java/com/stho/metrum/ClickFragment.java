package com.stho.metrum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/*
   Fragment to define beats per minute by clicking twice to the screen.
 */
public class ClickFragment extends Fragment implements View.OnTouchListener {

    private long first = 0;
    private long second = 0;
    private static final long TOLERANCE = 123; // may too much, but it helps.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.click_fragment, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final long third = System.currentTimeMillis();

                long a = third - second;
                long b = second - first;

                if (TOLERANCE < a && Math.abs(a - b) < TOLERANCE) {
                    INotifyListener listener = (INotifyListener) getActivity();
                    if (listener != null)
                        listener.onNotifyMillis(a);
                }
                first = second;
                second = third;
                break;
        }

        return true;
    }
}


