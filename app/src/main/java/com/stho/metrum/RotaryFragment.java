package com.stho.metrum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class RotaryFragment extends Fragment implements INotifyListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rotary_fragment, container, false);
        RotaryView rotary = (RotaryView) rootView.findViewById(R.id.rotary);
        rotary.setNotifyListener(this);
        return rootView;
    }

    @Override
    public void onNotifyAngle(double angle) {
        INotifyListener listener = (INotifyListener) getActivity();
        if (listener != null)
            listener.onNotifyAngle(angle);
    }

    @Override
    public void onNotifyMillis(long millis) {
        // ignore.
    }

    @Override
    public void onNotifyPlayerStateChanging(boolean bool) {
        // ignore.
    }

    @Override
    public void onNotifyMotionEvent(String viewKey, MotionEvent e) {
        // pass
    }

    @Override
    public void onNotifyTempoChanging(int i) {
        //
    }
}
