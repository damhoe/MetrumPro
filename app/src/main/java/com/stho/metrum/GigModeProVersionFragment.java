package com.stho.metrum;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.String.format;

public class GigModeProVersionFragment extends Fragment implements IUpdateableFragment {

    private static final TempoTrackSets tempoTrackSets = TempoTrackSets.create();
    private static final int DELAY = 20;

    private MainActivity mainActivity;
    private boolean animationRunState = false;

    private long lastTime;
    private long startTime;
    private long currentTime;

    private float trainDirection = 1;
    private double trainXPosition = 0;
    private float startDirection;
    private double startPosition;
    private double startPhase;
    private Handler handler = new Handler();

    private int checkedItem = 100;
    private int checkedSet = 100;
    private TempoTrack currentTempoTrack;

    private class ViewHolder {
        com.stho.metrum.TrainImageView train;
        GridView tracksGridView;
        Button btnPlayerState;
        ImageButton btnNextSet;
        ImageButton btnPreviousSet;
        TextView setNumber;
        TextView trackNameTextView;
    }

    private ViewModel viewModel = new ViewModel();
    private GigModeProVersionFragment.ViewHolder viewHolder = new GigModeProVersionFragment.ViewHolder();

    private int currentSetNumber = 2;
    private ArrayList<TempoTrack> currentSet = tempoTrackSets.getTracks(currentSetNumber);
    private ArrayList<TempoTrack> tempoTracks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gig_mode_fragment_pro_version, container, false);
        mainActivity = (MainActivity) this.getActivity();
        viewHolder.train = rootView.findViewById(R.id.trainView);
        viewHolder.tracksGridView = rootView.findViewById(R.id.track_grid_view);
        viewHolder.btnPlayerState = rootView.findViewById(R.id.gig_mode_pro_version_start_stop_button);
        viewHolder.btnNextSet = rootView.findViewById(R.id.btn_next_set);
        viewHolder.btnPreviousSet = rootView.findViewById(R.id.btn_previous_set);
        viewHolder.setNumber = rootView.findViewById(R.id.set_number_view);
        viewHolder.trackNameTextView = rootView.findViewById(R.id.track_name_text_view);

        viewHolder.tracksGridView.setSelector(R.drawable.selector_grid_view);

        viewHolder.trackNameTextView.setText(" ");

        viewHolder.btnPlayerState.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.btnNextSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSetNumber += 1;

                if (currentSetNumber > 4) {
                    currentSetNumber = 4;
                }

                currentSet = tempoTrackSets.getTracks(currentSetNumber);
                updateUI();
            }
        });

        viewHolder.btnPreviousSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSetNumber -= 1;

                if (currentSetNumber < 1) {
                    currentSetNumber = 1;
                }

                currentSet = tempoTrackSets.getTracks(currentSetNumber);
                updateUI();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        viewHolder.tracksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentTempoTrack = currentSet.get(position);

                if ((viewModel.getRunState()) && ((checkedItem == position) && (checkedSet == currentSetNumber))) {
                    assert mainActivity != null;

                    mainActivity.onNotifyPlayerStateChanging(false);
                    //viewHolder.tracksGridView.getChildAt(position).setBackground(getResources().getDrawable(R.drawable.grid_view_background));
                    updateUI();

                } else {
                    TempoTrack currentTempoTrack = currentSet.get(position);
                    viewModel.setBeatsPerMinute(currentTempoTrack.getBeatsPerMinute());
                    assert mainActivity != null;
                    if (!viewModel.getRunState()) {
                        mainActivity.onNotifyPlayerStateChanging(true);
                        updateUI();
                    }

                    viewHolder.tracksGridView.setSelection(position);
                    viewHolder.tracksGridView.setItemChecked(position, true);
                    checkedItem = position;
                    checkedSet = currentSetNumber;

                    //for (int k = 0; k < viewHolder.tracksGridView.getChildCount(); k++) {
                    //    viewHolder.tracksGridView.getChildAt(k).setBackground(getResources().getDrawable(R.drawable.grid_view_background));
                    //}

                    //viewHolder.tracksGridView.getChildAt(position).setBackground(getResources().getDrawable(R.drawable.grid_view_background_pressed));
                }
            }
        });

        updateUI();
        return rootView;
    }

    private void startAnimationThread(){

//        if (!viewModel.getRunState()){
//            mainActivity.onNotifyPlayerStateChanging(true);
//        }

        startTime = System.currentTimeMillis();
        lastTime = startTime;
        startPosition = trainXPosition;
        startDirection = trainDirection;

        startPhase = Math.asin(trainXPosition);
        if (trainDirection < 0) {
            startPhase += Math.PI;
        }

        GigModeProVersionFragment.this.handler.post(new Runnable() {
            @Override
            public void run() {
                    if (viewModel.getRunState()) {


                        long timeDelta = System.currentTimeMillis() - startTime;
                        //currentTime = System.currentTimeMillis();

                        assert viewModel != null;
                        float timePerBeat = 60000f / viewModel.getBeatsPerMinute();
//                        trainXPosition += trainDirection * (currentTime - lastTime) * 1/timePerBeat;
//                        if (trainXPosition > 1) {
//                            trainXPosition = 1;
//                            trainDirection = -1;
//                        }
//
//                        if (trainXPosition < 0) {
//                            trainDirection = 1;
//                            trainXPosition = 0;
//                        }
//
//                        lastTime = currentTime;

                        trainXPosition = Math.sin(Math.PI * timeDelta / timePerBeat + startPhase);
                        trainDirection = (float) Math.signum(Math.cos(Math.PI * timeDelta / timePerBeat + startPhase));

                      /*  int bpm = viewModel.getBeatsPerMinute();
                        float timePerBeat = 60000f / bpm;

                        float rest = timeDelta % timePerBeat;

                        if  ((int) (timeDelta - rest) % 2 == 0) {
                            trainDirection = 1;
                        }
                        if  ((int) (timeDelta - rest) % 2 == 1) {
                            trainDirection = -1;
                        }

                        if (trainDirection == 1) {
                            trainXPosition = rest/timePerBeat;
                        }

                        if (trainDirection == -1) {
                            trainXPosition = 1 - rest/timePerBeat;
                        }*/
                        /*if (trainXPosition > 1) {
                            trainDirection = -1;
                            trainXPosition = 1;
                        }
                        if (trainXPosition < 0) {
                            trainDirection = 1;
                            trainXPosition = 0;
                        }*/

                        viewHolder.train.moveX((float) (1 + trainXPosition) * 0.5f, trainDirection);
                        handler.postDelayed(this, DELAY);
                    }
            }
        });
    }

    private void stopAnimationThread() {

        if (handler != null) {
            this.handler.removeCallbacks(null);
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

    private void updateUI(){

        String input = (String) viewHolder.setNumber.getText();
        if (Integer.parseInt(input.substring(input.length() - 1)) != currentSetNumber) {
            TempoTrackAdapter trackListAdapter = new TempoTrackAdapter(getContext(), currentSet);
            //ArrayAdapter<TempoTrack> trackListAdapter = new ArrayAdapter<TempoTrack>(getContext(), R.layout.grid_item, R.id.textViewListItem, currentSet);
            viewHolder.tracksGridView.setAdapter(trackListAdapter);
            viewHolder.setNumber.setText(format("%s %d", "Set", currentSetNumber));
        }

        if (currentTempoTrack != null) {
            viewHolder.trackNameTextView.setText(currentTempoTrack.getName());
        }

        if (viewModel.getRunState()) {
            //viewHolder.btnPlayerState.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            //viewHolder.btnPlayerState.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void translateImage(int xTranslate, int yTranslate)
    {

    }

    private void openTempoList(MainActivity activity, int setNumber) {
        activity.defineTrack(setNumber);
        //startActivityForResult(getActivity().getIntent(), CODE);
    }
}
