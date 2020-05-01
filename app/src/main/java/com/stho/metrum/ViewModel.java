package com.stho.metrum;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collection;

public class ViewModel {

    private ArrayList<ViewModel.OnChangeListener> listeners = new ArrayList<>();
    private boolean isRunning = false;
    private int beatsPerMinute;
    private int beatsSplit;
    private final ArrayList<Integer> beats = new ArrayList<>();
    private String title = "Default";
    private int beatsPerBar;

    static final int NONE = 0x00;
    static final int TICK = 0x01;
    static final int TOCK = 0x02;
    static final int BASS = 0x04;
    static final int CYMBAL = 0x08;
    static final int CRACK = 0x10;
    static final int DEFAULT_BPM = 60;

    ViewModel() {
        setBeatsPerMinute(DEFAULT_BPM);
        setMetrum();
    }

    boolean getRunState() { return isRunning; }

    void setRunState(boolean state) { isRunning = state; }

    int getBeatsPerMinute() {
        return beatsPerMinute;
    }

    void setBeatsPerMinute(int beatsPerMinute) {
        this.beatsPerMinute = beatsPerMinute;
    }

    int getBeatsSplit() {
        return beatsSplit;
    }

    int getSplittedBeatsPerMinute() {
        return beatsPerMinute * beatsSplit;
    }

    int getNumberSamplesPerSplittedBeat() {
        return AudioGenerator.SAMPLES_PER_MINUTE / getSplittedBeatsPerMinute();
    }

    void setBeatsSplit(int beatsSplit) {
        this.beatsSplit = beatsSplit;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    ArrayList<Integer> getBeats() {
        return beats;
    }

    int getBeatsPerBar() { return beatsPerBar; }

    void setBeatsPerBar(int beatsPerBar) { this.beatsPerBar = beatsPerBar; }

    void setBeats(Collection<Integer> beats) {
        this.beats.clear();
        this.beats.addAll(beats);
        notifyArrayListChanged();
    }

    void setBeats(int[] beatsArray) {
        this.beats.clear();
        if (beatsArray != null) {
            for (int beat : beatsArray) {
                this.beats.add(beat);
            }
        }
        notifyArrayListChanged();
    }

    int getBeat(int position) {
        return (position < beats.size()) ? beats.get(position) : NONE;
    }

    int size() {
        return beats.size();
    }

    boolean addBeat(final int position, final int value) {
        if (position < beats.size()) {
            int beat = beats.get(position);
            if ((beat & value) == 0) {
                beat += value;
                beats.set(position, beat | value);
                notifyArrayListChanged();
                return true;
            }
        }
        return false;
    }

    boolean removeBeat(final int position, final int value) {
        if (position < beats.size()) {
            int beat = beats.get(position);
            if ((beat & value) == value) {
                beat -= value;
                beats.set(position, beat);
                notifyArrayListChanged();
                return true;
            }
        }
        return false;
    }

    void addBeat() {
        beats.add(TICK);
        notifyArrayListChanged();
    }

    void deleteBeat(int position) {
        if (position >= 0 && position < beats.size()) {
            beats.remove(position);
            notifyArrayListChanged();
        }
    }

    int[] getBeatsArray() {
        int[] array = new int[beats.size()];

        for (int position = 0; position < array.length; position++)
            array[position] = getBeat(position);

        return array;
    }

    public interface OnChangeListener {
        void onChange();
    }

    void setOnChangeListener(ViewModel.OnChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyArrayListChanged() {
        for (int i = 0; i < listeners.size(); i++) {
            ViewModel.OnChangeListener listener = listeners.get(i);
            if (listener != null)
                listener.onChange();
        }
    }

    /*
      TODO: icon should really visualize a combination of 4 basic features or nothing
     */
    static int getImageFor(int beat) {
        switch (beat) {
            case ViewModel.BASS:
                return R.drawable.knob_small_bass;

            case ViewModel.CYMBAL:
                return R.drawable.knob_small_cymbal;

            case ViewModel.TICK:
                return R.drawable.knob_small_red;

            case ViewModel.TOCK:
                return R.drawable.knob_small_orange;

            case ViewModel.CRACK:
                return R.drawable.knob_small_crack;

            case ViewModel.NONE:
                return R.drawable.knob_small;

            default:
                if ((beat & ViewModel.BASS) == ViewModel.BASS)
                    return R.drawable.knob_small_bass;

                else if ((beat & ViewModel.CYMBAL) == ViewModel.CYMBAL)
                    return R.drawable.knob_small_cymbal;

                else if ((beat & ViewModel.TICK) == ViewModel.TICK)
                    return R.drawable.knob_small_red;

                else if ((beat & ViewModel.TOCK) == ViewModel.TOCK)
                    return R.drawable.knob_small_orange;

                else
                    return R.drawable.knob_small;
        }
    }

    public void setMetrum() {
        setBeatsSplit(1);
        setBeats(new int[]{TICK, TICK, TICK, TICK});
    }

    public void setWaltz() {
        setBeatsPerMinute(70);
        setBeatsSplit(1);
        setBeats(new int[]{ BASS | TICK | TOCK | CYMBAL, TOCK, TICK | CYMBAL });
    }

    public void setSalsa() {
        setBeatsPerMinute(165);
        setBeatsSplit(2);
        // 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 -
        //     t       c c     t       c c
        // k     k     k   k     k     k
        // b       b       b       b
        setBeats(new int[]{BASS | TICK, CRACK, TOCK, TICK, BASS, NONE, TICK | CYMBAL, CYMBAL});
    }

    public void setChaCha() {
        setBeatsPerMinute(135);
        setBeatsSplit(2);
        // 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 -
        // c c   c c c t t c c   c c c t t
        // k   k k k   k k k   k k k   k k
        // b       b       b       b
        setBeats(new int[]{BASS | TICK | CYMBAL, CYMBAL, TICK, TICK | CYMBAL, BASS | TICK | CYMBAL, CYMBAL, TOCK | TICK, TOCK | TICK});
    }

    public void setSlowFox() {
        setBeatsPerMinute(55);
        setBeatsSplit(2);
        setBeats(new int[]{BASS | TICK | CYMBAL, CYMBAL, BASS | TOCK | CYMBAL, CYMBAL, BASS |CYMBAL, CYMBAL, TICK | TOCK | CYMBAL, CYMBAL});
    }

    public Intent getIntent() {
        Intent intent = new Intent();
        putExtra(intent);
        return intent;
    }

    public Intent getIntent(android.content.Context packageContext, java.lang.Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        putExtra(intent);
        return intent;
    }

    private void putExtra(Intent intent) {
        intent.putExtra(MainActivity.INTENT_KEY_BEATS, getBeatsArray());
        intent.putExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, getBeatsPerMinute());
        intent.putExtra(MainActivity.INTENT_KEY_BEATS_SPLIT, getBeatsSplit());
        intent.putExtra(MainActivity.INTENT_KEY_TITLE, getTitle());
    }

    public void fromIntent(Intent intent) {
        setBeats(intent.getIntArrayExtra(MainActivity.INTENT_KEY_BEATS));
        setBeatsPerMinute(intent.getIntExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, beatsPerMinute));
        setBeatsSplit(intent.getIntExtra(MainActivity.INTENT_KEY_BEATS_SPLIT, beatsSplit));
        setTitle(intent.getStringExtra(MainActivity.INTENT_KEY_TITLE));
    }
}


