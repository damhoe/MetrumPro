package com.stho.metrum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyFragment extends Fragment {

    static final int CODE = 4;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.empty_fragment, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();

        view.findViewById(R.id.TextViewSetOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity, 1);
            }
        });
        view.findViewById(R.id.TextViewSetTwo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity, 2);
            }
        });
        view.findViewById(R.id.TextViewSetThree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity,3);
            }
        });
        view.findViewById(R.id.TextViewSetFour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity,4);
            }
        });
        return view;
    }

    private void openTempoList(MainActivity activity, int setNumber) {
        activity.defineTrack(setNumber);
        //startActivityForResult(getActivity().getIntent(), CODE);
    }
}


