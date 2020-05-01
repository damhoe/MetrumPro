package com.stho.metrum;

import android.content.Context;
import android.nfc.FormatException;

import java.io.IOException;
import java.util.ArrayList;

public class MetrumAudioPlayer {

    private AudioGenerator audio = null;
    private final Context context;

    public MetrumAudioPlayer(Context context) {
        this.context = context;
    }

    public void release() {
        if (audio != null) {
            audio.release();
            audio = null;
        }
    }

    public void prepareAudio() throws IOException, FormatException {
        audio = new AudioGenerator(this.context);
        audio.createSoundBuffer(ViewModel.TICK, R.raw.metrum);
        audio.createSoundBuffer(ViewModel.TOCK, R.raw.drums);
        audio.createSoundBuffer(ViewModel.BASS, R.raw.bass);
        audio.createSoundBuffer(ViewModel.CYMBAL, R.raw.cymbal);
        audio.createSoundBuffer(ViewModel.CRACK, R.raw.crack);
        audio.create();
    }

    public int play(int key) throws IOException, FormatException {
        if (key == ViewModel.NONE) {
            return 0;
        }
        ensureAudio(key);
        return audio.playSound(key);
    }

    public void playSilence(int samples) {
        audio.playSilence(samples);
    }

    private static final int MAX_SAMPLES = 10000;

    public void play(int key, int samples) throws IOException, FormatException {
        int remaining = samples;
        if (key != ViewModel.NONE) {
            remaining -= play(key);
        }
        while (remaining >= MAX_SAMPLES) {
            remaining -= MAX_SAMPLES;
            playSilence(MAX_SAMPLES);
        }
        if (remaining > 0) {
            playSilence(remaining);
        }
    }

    private void ensureAudio(int key) throws IOException, FormatException {
        if (!audio.hasKey(key)) {
            prepareAudio(key);
        }
    }

    private void prepareAudio(int key) throws IOException, FormatException {
        final ArrayList<Integer> keys = getSingleKeys(key);
        switch (keys.size()) {

            case 2:
                audio.createJoinedSoundBuffer(key, keys.get(0), keys.get(1));
                break;

            case 3:
                audio.createJoinedSoundBuffer(key, keys.get(0), keys.get(1), keys.get(2));
                break;

            case 4:
                audio.createJoinedSoundBuffer(key, keys.get(0), keys.get(1), keys.get(2), keys.get(3));
                break;

            case 5:
                audio.createJoinedSoundBuffer(key, keys.get(0), keys.get(1), keys.get(2), keys.get(3), keys.get(4));
                break;
        }
    }

    private ArrayList<Integer> getSingleKeys(int key) {
        final ArrayList<Integer> keys = new ArrayList<>();
        for (int single : new int[]{ ViewModel.TICK, ViewModel.TOCK, ViewModel.BASS, ViewModel.CYMBAL, ViewModel.CRACK }) {
            if ((key & single) == single) {
                keys.add(single);
            }
        }
        return keys;
    }

}
