package com.stho.metrum;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LiveModeFragment extends Fragment implements IUpdateableFragment {

    private class ViewHolder{
        LinearLayout background;
        TextView firstSet;
        TextView secondSet;
        TextView thirdSet;
        TextView bonusSet;
    }

    private static final int DELAY_LONG = 100;
    private final static int DELAY_SHORT = 50;

    private ViewModel viewModel;
    private ViewHolder viewHolder = new ViewHolder();

    private final Handler handler = new Handler();
    private boolean isBright;
    private double nextBeatTime;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.live_mode_fragment, container,false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        viewHolder.background = rootView.findViewById(R.id.live_mode_background);
        viewHolder.firstSet = rootView.findViewById(R.id.first_set_view);
        viewHolder.secondSet = rootView.findViewById(R.id.second_set_view);
        viewHolder.thirdSet = rootView.findViewById(R.id.third_set_view);
        viewHolder.bonusSet = rootView.findViewById(R.id.bonus_set_view);

        isBright = false;
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
        stopAnimationThread();
        if (viewModel.getRunState()) {
            startAnimationThread();
        } else {
            stopAnimationThread();
        }
    }

    private void openTempoList(MainActivity activity, int setNumber) {
        activity.defineTrack(setNumber);
    }

    private void startAnimationThread() {

        isBright = false;
        nextBeatTime = System.currentTimeMillis();

        LiveModeFragment.this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (viewModel.getRunState()) {
                    long delay;

                    if (isBright) {
                        blink(false);
                        delay = (long) (nextBeatTime - System.currentTimeMillis());
                    } else {
                        blink(true);
                        nextBeatTime += 60000.0 / viewModel.getBeatsPerMinute();
                        delay = DELAY_SHORT;
                    }
                    if (delay < 0) {
                        delay = 0;
                    }
                    handler.postDelayed(this, delay);
                }
            }
        });
    }

    private void stopAnimationThread() {
        if (handler != null) {
            handler.removeCallbacks(null);
        }
        if (viewHolder.background != null) {
            blink(false);
        }
    }

    private void blink(boolean lightOn) {
        if (lightOn) {
            viewHolder.background.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            isBright = true;
            Log.v("bright", Boolean.toString(isBright));
        } else {
            viewHolder.background.setBackgroundColor(getResources().getColor(R.color.colorBackground));
            isBright = false;
            Log.v("bright", Boolean.toString(isBright));
        }
    }

    private void updateUI(){

    }
}
