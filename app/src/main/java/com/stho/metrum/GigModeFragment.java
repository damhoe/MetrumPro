package com.stho.metrum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GigModeFragment extends Fragment implements IUpdateableFragment {

    private class ViewHolder {
        TextView firstSet;
        TextView secondSet;
        TextView thirdSet;
        TextView bonusSet;
    }

    private ViewModel viewModel;
    private ViewHolder viewHolder = new ViewHolder();
    private int currentSetNumber;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gig_mode_fragment, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        viewHolder.firstSet = rootView.findViewById(R.id.set_one_view);
        viewHolder.secondSet = rootView.findViewById(R.id.set_two_view);
        viewHolder.thirdSet = rootView.findViewById(R.id.set_three_view);
        viewHolder.bonusSet = rootView.findViewById(R.id.set_bonus_view);

        viewHolder.firstSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewHolder.firstSet.setBackgroundColor(getResources().getColor(R.color.colorListAccent));
                openTempoList(mainActivity, 1);
            }
        });

        viewHolder.secondSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity, 2);
            }
        });

        viewHolder.thirdSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity, 3);
            }
        });

        viewHolder.bonusSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempoList(mainActivity, 4);
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                assert mainActivity != null;
                mainActivity.onNotifyPlayerStateChanging(false);
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void updateViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private void openTempoList(MainActivity activity, int setNumber) {
        activity.defineTrack(setNumber);
        //startActivityForResult(getActivity().getIntent(), CODE);
    }

//    private void updateUI(MainActivity mainActivity) {
//        android.support.v4.app.FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
//        switch (currentSetNumber) {
//            case 1: {
//                ft.replace(R.id.main_fragment_spaceholder, new TempoTrackListFragment(), FRAGMENT_KEY);
//                break;
//            }
//            case 2: {
//                ft.replace(R.id.main_fragment_spaceholder, new LiveModeFragment(), FRAGMENT_KEY);
//                break;
//            }
//            case 3: {
//                ft.replace(R.id.main_fragment_spaceholder, new ClassicModeFragment(), FRAGMENT_KEY);
//                break;
//            }
//        }
//        ft.commitNow();
//    }
//
//    private void changeScreenColor() {
//        if (screenColor == 0) {
//            screenColor = 1;
//            viewHolder.fragment.updateViewModel();
//            viewHolder.mainLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//        } else if (screenColor == 1) {
//            screenColor = 0;
//            viewHolder.mainLayout.setBackgroundColor(getResources().getColor(R.color.colorBackground));
//        }
//    }
}
