package com.stho.metrum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VisualizationFragment extends android.support.v4.app.Fragment {

    private ClassicPointerView pointer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.classic_visualisation_fragment, container, false);
        this.pointer = (ClassicPointerView) rootView.findViewById(R.id.pointer);
        return rootView;
    }

    public void rotate(float angle) {
        if (this.pointer != null) {
            pointer.angle += angle;
        }
    }

    public void startAnimation(float beatsPerMinute, String mode) {
        if (this.pointer != null) {
            pointer.angle += 10;
        }
    }
}
