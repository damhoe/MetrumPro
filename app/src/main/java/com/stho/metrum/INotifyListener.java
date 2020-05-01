package com.stho.metrum;

import android.view.MotionEvent;

interface INotifyListener {
    void onNotifyAngle(double angle);
    void onNotifyMillis(long millis);
    void onNotifyPlayerStateChanging(boolean newState);
    void onNotifyMotionEvent(String viewKey, MotionEvent e);
    void onNotifyTempoChanging(int newTempo);
}
