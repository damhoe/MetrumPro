package com.stho.metrum;

import java.util.ArrayList;
import java.util.Date;

public class Metrum {
    private final long id;
    private final String title;
    private final Date date;
    private final int beatsPerMinute;
    private final ArrayList<Integer> beats;


    Metrum(final long id, final String title, final Date date, final int beatsPerMinute, final ArrayList<Integer> beats) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.beatsPerMinute = beatsPerMinute;
        this.beats = beats;
    }

    long getId() { return id; };
    String getTitle() { return title; }
    Date getDate() { return date; }
    int getBeatsPerMinute() { return beatsPerMinute; }
    ArrayList<Integer> getBeats() { return beats; }
}
