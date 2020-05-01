package com.stho.metrum;

import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements INotifyListener {

    static final int DEFINE_FREQUENCY_REQUEST_CODE = 0;
    static final int DEFINE_BEATS_REQUEST_CODE = 1;
    static final int MANAGE_STORAGE_REQUEST_CODE = 2;
    static final int DEFINE_TRACK_REQUEST_CODE = 3;
    static final int SETTINGS_REQUEST_CODE = 4;
    static final String INTENT_KEY_BEATS_PER_MINUTE = "BEATS_PER_MINUTE";
    static final String INTENT_KEY_BEATS_SPLIT = "BEATS_SPLIT";
    static final String INTENT_KEY_BEATS = "BEATS";
    static final String INTENT_KEY_TITLE = "TITLE";
    static final String INTENT_KEY_SET = "SET_NUMBER";
    static final String INTENT_KEY_TRACK_NAME = "TRACK_NAME";
    static final String INTENT_KEY_DANCE = "DANCE";
    static final String POINTER_VIEW_KEY = "POINTER_VIEW";

    private class ViewHolder
    {
        IUpdateableFragment fragment;
    }

    private final ViewHolder viewHolder = new ViewHolder();
    private final String FRAGMENT_KEY = "MAIN_FRAGMENT";
    private Menu menu;

    private boolean isRunning = false;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer = null;
    private int screenColor = 0;
    private boolean beatOne = false, beatTwo = false, beatThree = false, beatFour = false;
    private String visualizationMode;
    private String mode = "live"; // possible values "standard", "classic", "live"

    final ViewModel viewModel = new ViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null)
        {
            viewModel.setBeats(savedInstanceState.getIntArray(MainActivity.INTENT_KEY_BEATS));
            viewModel.setBeatsPerMinute(savedInstanceState.getInt(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, 120));
            viewModel.setBeatsSplit(savedInstanceState.getInt(MainActivity.INTENT_KEY_BEATS_SPLIT, 1));
            viewModel.setTitle(savedInstanceState.getString(MainActivity.INTENT_KEY_TITLE));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateMode();
        updateData();
    }

    void defineFrequency() {
        Intent intent = viewModel.getIntent(MainActivity.this, DefineFrequencyActivity.class);
        startActivityForResult(intent, DEFINE_FREQUENCY_REQUEST_CODE);
    }

    void defineBeats() {
        Intent intent = viewModel.getIntent(MainActivity.this, DefineBeatsActivity.class);
        startActivityForResult(intent, DEFINE_BEATS_REQUEST_CODE);
    }

    void manageStorage() {
        Intent intent = viewModel.getIntent(MainActivity.this, StorageActivity.class);
        startActivityForResult(intent, MANAGE_STORAGE_REQUEST_CODE);
    }

    void defineTrack(int setNumber) {
        Intent intent = new Intent(MainActivity.this, TempoTrackListActivity.class);
        intent.putExtra(INTENT_KEY_SET, setNumber);
        startActivityForResult(intent, DEFINE_TRACK_REQUEST_CODE);
    }

    void save() {
        // TODO: define activity ...
    }

    public void updateMode() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (mode) {
            case "standard": {
                ft.replace(R.id.main_fragment_spaceholder, new StandardModeFragment(), FRAGMENT_KEY);
                break;
            }
            case "live": {
                ft.replace(R.id.main_fragment_spaceholder, new GigModeProVersionFragment(), FRAGMENT_KEY);
                break;
            }
            case "classic": {
                ft.replace(R.id.main_fragment_spaceholder, new ClassicModeFragment(), FRAGMENT_KEY);
                break;
            }
            //case "gig": {
            //    ft.replace(R.id.main_fragment_spaceholder, new TempoTrackListFragment(), FRAGMENT_KEY);
            //}
        }
        ft.commit();
        viewHolder.fragment = (IUpdateableFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY);
    }

    public void updateData() {
        if (viewHolder.fragment != null){
            viewHolder.fragment.updateViewModel(viewModel);
        }
    }

    private void updateUI() {
        if (menu != null) {
            switch (mode) {
                case "standard": {
                    menu.findItem(R.id.action_standard_mode).setIcon(R.drawable.standard_pressed);
                    menu.findItem(R.id.action_live_mode).setIcon(R.drawable.live_unpressed);
                    menu.findItem(R.id.action_classic_mode).setIcon(R.drawable.classis_unpressed);
                    break;
                }
                case "live": {
                    menu.findItem(R.id.action_live_mode).setIcon(R.drawable.live_pressed);
                    menu.findItem(R.id.action_standard_mode).setIcon(R.drawable.standard_unpressed);
                    menu.findItem(R.id.action_classic_mode).setIcon(R.drawable.classis_unpressed);
                    break;
                }
                case "classic": {
                    menu.findItem(R.id.action_classic_mode).setIcon(R.drawable.classis_pressed);
                    menu.findItem(R.id.action_live_mode).setIcon(R.drawable.live_unpressed);
                    menu.findItem(R.id.action_standard_mode).setIcon(R.drawable.standard_unpressed);
                    break;
                }
                case "gig": {
                    menu.findItem(R.id.action_classic_mode).setIcon(R.drawable.classis_unpressed);
                    menu.findItem(R.id.action_live_mode).setIcon(R.drawable.live_unpressed);
                    menu.findItem(R.id.action_standard_mode).setIcon(R.drawable.standard_unpressed);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DEFINE_FREQUENCY_REQUEST_CODE:
                    viewModel.setBeatsPerMinute(data.getIntExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, 100));
                    updateData();
//                    viewHolder.frequency.setText(String.format(Locale.ENGLISH, "%d", viewModel.getBeatsPerMinute()));
//                    viewHolder.tempo.setText(Tempi.getNameByBeats(viewModel.getBeatsPerMinute()));
//                    viewHolder.track.setText("default");
//                    viewHolder.dance.setText("undefined");
                    break;

                case DEFINE_BEATS_REQUEST_CODE:
                    viewModel.fromIntent(data);
                    updateData();
//                    viewHolder.frequency.setText(String.format(Locale.ENGLISH, "%d", viewModel.getBeatsPerMinute()));
//                    viewHolder.tempo.setText(Tempi.getNameByBeats(viewModel.getBeatsPerMinute()));
//                    viewHolder.track.setText("default");
//                    viewHolder.dance.setText("undefined");
                    break;

                case MANAGE_STORAGE_REQUEST_CODE:
                    viewModel.fromIntent(data);
                    updateData();
//                    viewHolder.frequency.setText(String.format(Locale.ENGLISH, "%d", viewModel.getBeatsPerMinute()));
//                    viewHolder.tempo.setText(Tempi.getNameByBeats(viewModel.getBeatsPerMinute()));
//                    viewHolder.track.setText("default");
//                    viewHolder.dance.setText("undefined");
                    break;

                case DEFINE_TRACK_REQUEST_CODE:
                    viewModel.setBeatsPerMinute(data.getIntExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, 100));
                    updateData();
//                    viewHolder.frequency.setText(String.format(Locale.ENGLISH, "%d", viewModel.getBeatsPerMinute()));
//                    viewHolder.tempo.setText(Tempi.getNameByBeats(viewModel.getBeatsPerMinute()));
//                    viewHolder.track.setText(data.getStringExtra(MainActivity.INTENT_KEY_TRACK_NAME));
//                    viewHolder.dance.setText(data.getStringExtra(MainActivity.INTENT_KEY_DANCE));
                    start();
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntArray(MainActivity.INTENT_KEY_BEATS, viewModel.getBeatsArray());
        outState.putInt(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, viewModel.getBeatsPerMinute());
        outState.putString(MainActivity.INTENT_KEY_TITLE, viewModel.getTitle());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    private void start() {
        viewModel.setRunState(true);
        updateData();
        startPlayerThread();
//        startVisualizationThread();
    }

//    private void startVisualizationThread() {
////        new Thread(new Runnable() {
////
////            private String visualization_mode = "classic_mode";
////            private long currentTime = System.currentTimeMillis();
////
////            @Override
////            public void run() {
////                while (isRunning) {
////                    blink();
////                }
//////                visualizationFragment.startAnimation(viewModel.getBeatsPerMinute(), visualization_mode);
////            }
////
////            private void blink() {
////                MainActivity.this.handler.post(new Runnable() {
////                    @Override
////                    public void run() {
////
////                    }
////                });
////            }
////
////        }).start();
////    }

    private void startPlayerThread() {
        new Thread(new Runnable() {

            private MetrumAudioPlayer player = null;
            private int position = 0;

            public void run() {
                try {
                    player = new MetrumAudioPlayer(MainActivity.this);
                    player.prepareAudio();
                    while (viewModel.getRunState()) {
                        beat();
                        updateUI();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
                }
            }

            /*
              Note, the audio device "blocks" the write request for the time that the buffer is full.
              Therefore we can write continously here in that background thread.
              */
            private void beat() throws IOException, FormatException {
                final ArrayList<Integer> beats = viewModel.getBeats();
                int samples = viewModel.getNumberSamplesPerSplittedBeat();
                int key = viewModel.getBeat(position);
                player.play(key, samples);
                position++;
                if (position >= beats.size())
                    position = 0;
            }

            /*
              Note, the audio device plays the sound with a small delay.
              Therefore we should expect that updateUI and playing the playTick are NOT at the exactly same moment.
              We may play around with the audio buffer size.
             */
            private void updateUI() {
                MainActivity.this.handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        updateColor();
//                        if (beatOne) {
//                            beatOne = false;
//                            beatTwo = true;
//                        } else if (beatTwo) {
//                            beatTwo = false;
//                            beatThree = true;
//                        } else if (beatThree) {
//                            beatThree = false;
//                            beatFour = true;
//                        } else if (beatFour) {
//                            beatFour = false;
//                            beatOne = true;
//                        }
                        //changeLambColor();
                    }
                });
            }
        }).start();
    }

    private void stop() {
        viewModel.setRunState(false);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

//    private void updateColor() {
//        viewHolder.frequency.setTextColor(ColorManager.getColor());
//    }
//
//    private void changeLambColor() {
//        if (beatOne) {
//            viewHolder.lambOne.setBackgroundColor(getResources().getColor(R.color.colorListAccent));
//            viewHolder.lambFour.setBackgroundColor(getResources().getColor(R.color.colorList));
//        } else if (beatTwo) {
//            viewHolder.lambTwo.setBackgroundColor(getResources().getColor(R.color.colorListAccent));
//            viewHolder.lambOne.setBackgroundColor(getResources().getColor(R.color.colorList));
//        } else if (beatThree) {
//            viewHolder.lambThree.setBackgroundColor(getResources().getColor(R.color.colorListAccent));
//            viewHolder.lambTwo.setBackgroundColor(getResources().getColor(R.color.colorList));
//        } else if (beatFour) {
//            viewHolder.lambFour.setBackgroundColor(getResources().getColor(R.color.colorListAccent));
//            viewHolder.lambThree.setBackgroundColor(getResources().getColor(R.color.colorList));
//        }
//    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        Log.d("menu log", Integer.toString(menu.getItem(1).getItemId()));
        updateUI();
        return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                return true;
            }
            case R.id.action_standard_mode: {
                mode = "standard";
                updateMode();
                updateUI();
                break;
            }
            case R.id.action_live_mode: {
                mode = "live";
                updateMode();
                updateUI();
                break;
            }
            case R.id.action_classic_mode: {

                mode = "classic";
                updateMode();
                updateUI();
                break;
            }
        }
        updateUI();
        return super.onOptionsItemSelected(item);
    }

    private float beats = 0;
    private final static float FACTOR_ANGLE_TO_BEATS = 0.13f;
    private final static float MAX_BEATS = 230;
    private final static float MIN_BEATS = 30;

    @Override
    public void onNotifyAngle(double angle) {
        if (beats == 0)
            beats = viewModel.getBeatsPerMinute();

        beats += angle * FACTOR_ANGLE_TO_BEATS;

        if (beats > MAX_BEATS)
            beats = MAX_BEATS;

        if (beats < MIN_BEATS)
            beats = MIN_BEATS;

        setBeats();
        updateData();
    }

    @Override
    public void onNotifyMillis(long millis) {
        beats = 60000f / millis;
        setBeats();
        updateData();
    }

    @Override
    public void onNotifyPlayerStateChanging(boolean newState) {
        if (newState) {
            if (!viewModel.getRunState())
                start();
        }
        else stop();
        updateData();
    }

    @Override
    public void onNotifyMotionEvent(String viewKey, MotionEvent e) {
       // pass
    }

    @Override
    public void onNotifyTempoChanging(int newTempo) {
        viewModel.setBeatsPerMinute(newTempo);
        updateData();
    }

    private void setBeats() {
        int newBeatsPerMinute = (int) beats;
        if (newBeatsPerMinute != viewModel.getBeatsPerMinute()) {
            viewModel.setBeatsPerMinute(newBeatsPerMinute);
//            viewHolder.frequency.setText(String.format(Locale.ENGLISH, "%d", viewModel.getBeatsPerMinute()));
//            viewHolder.tempo.setText(Tempi.getNameByBeats(viewModel.getBeatsPerMinute()));
            crack();
        }
    }

    public void crack() {
        handler.post(new Runnable() {
            @Override
            public void run() {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.crack);
            if (mediaPlayer != null)
                mediaPlayer.start();
            }
        });
    }
}


