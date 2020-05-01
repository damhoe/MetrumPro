package com.stho.metrum;

public class TempoTrack {

    private int beatsPerMinute;
    private String name;
    private String dance;

    String getName() { return name; }
    String getDance() { return dance; }
    int getBeatsPerMinute() { return beatsPerMinute; }

    TempoTrack(String name, int beatsPerMinute, String dance) {
        this.name = name;
        this.dance = dance;
        this.beatsPerMinute = beatsPerMinute;
    }

    @Override
    public String toString() {
        return name + " - " +  dance;
    }
}
