package com.stho.metrum;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TempoTrackListFragment extends Fragment implements IUpdateableFragment {

    private static final TempoTrackSets tempoTrackSets = TempoTrackSets.create();

    private class ViewHolder{
        TextView firstSet;
        TextView secondSet;
        TextView thirdSet;
        TextView bonusSet;
        ImageButton startStopButton;
        ListView setList;
        LinearLayout background;
    }

    private final ViewHolder viewHolder = new ViewHolder();
    private ViewModel viewModel = new ViewModel();
    private ArrayList<TempoTrack> currentSet = tempoTrackSets.getTracks(1);
    private ArrayList<TempoTrack> tempoTracks;

    private static final int DELAY_LONG = 100;
    private final static int DELAY_SHORT = 50;

    private final Handler handler = new Handler();
    private boolean isBright;
    private double nextBeatTime;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gig_mode_fragment, container,false);
        final MainActivity mainActivity = (MainActivity) getActivity();

        tempoTracks = TempoTrackDataSource.getData(getContext());

        viewHolder.firstSet = rootView.findViewById(R.id.set_one_view);
        viewHolder.secondSet = rootView.findViewById(R.id.set_two_view);
        viewHolder.thirdSet = rootView.findViewById(R.id.set_three_view);
        viewHolder.bonusSet = rootView.findViewById(R.id.set_bonus_view);
        viewHolder.startStopButton = rootView.findViewById(R.id.gig_mode_start_stop_button);
        viewHolder.setList = rootView.findViewById(R.id.tempo_track_set_list_view);
        viewHolder.background = rootView.findViewById(R.id.gig_mode_fragment_background);
        viewHolder.setList.setSelector(R.drawable.selector);

        viewHolder.firstSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSet = tempoTrackSets.getTracks(1);
                updateUI();
            }
        });

        viewHolder.secondSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSet = tempoTrackSets.getTracks(2);
                updateUI();
            }
        });

        viewHolder.thirdSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSet = tempoTrackSets.getTracks(3);
                updateUI();
            }
        });

        viewHolder.bonusSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSet = tempoTracks;// tempoTrackSets.getTracks(4);
                updateUI();
            }
        });

        viewHolder.startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mainActivity != null;
                if (viewModel.getRunState()) {
                    mainActivity.onNotifyPlayerStateChanging(false);
                } else {
                    mainActivity.onNotifyPlayerStateChanging(true);
                }
                updateUI();
            }
        });
        updateUI();

        viewHolder.setList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TempoTrack currentTempoTrack = currentSet.get(position);
                viewModel.setBeatsPerMinute(currentTempoTrack.getBeatsPerMinute());
                assert mainActivity != null;
                if (!viewModel.getRunState()) {
                    mainActivity.onNotifyPlayerStateChanging(true);
                    updateUI();
                }

                viewHolder.setList.setSelection(position);
                viewHolder.setList.setItemChecked(position, true);
            }
        });

        return rootView;
    }

    private void updateUI() {
        ArrayAdapter<TempoTrack> trackListAdapter = new ArrayAdapter<TempoTrack>(getContext(), R.layout.list_item, R.id.textViewListItem, currentSet);
        viewHolder.setList.setAdapter(trackListAdapter);
        if (viewModel.getRunState()) {
            viewHolder.startStopButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            viewHolder.startStopButton.setImageResource(android.R.drawable.ic_media_play);
        }
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

    private void startAnimationThread() {

        isBright = false;
        nextBeatTime = System.currentTimeMillis();

        TempoTrackListFragment.this.handler.post(new Runnable() {
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
            viewHolder.background.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            isBright = false;
            Log.v("bright", Boolean.toString(isBright));
        }
    }
}
