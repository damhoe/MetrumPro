package com.stho.metrum;

import java.util.ArrayList;

class Tempo
{
    final String name;
    final int minBeatsPerMinute;
    final int maxBeatsPerMinute;
    final int defaultBeatsMinute;

    Tempo(String name, int minBeatsPerMinute, int maxBeatsPerMinute, int defaultBeatsMinute) {
        this.name = name;
        this.minBeatsPerMinute = minBeatsPerMinute;
        this.maxBeatsPerMinute = maxBeatsPerMinute;
        this.defaultBeatsMinute = defaultBeatsMinute;
    }
}

class Tempi extends ArrayList<Tempo>
{
    private static Tempi tempi = null;

    static Tempi getInstance() {
        if (tempi == null) {
            tempi = new Tempi();
            tempi.onCreate();
        }
        return tempi;
    }

    static int getDefaultBeatsByName(String name) {
        for (Tempo tempo : Tempi.getInstance()) {
            if (tempo.name.compareTo(name) == 0) {
                return tempo.defaultBeatsMinute;
            }
        }
        return 0;
    }

    static String getNameByBeats(int beatsPerMinute) {
        for (Tempo tempo : Tempi.getInstance()) {
            if (tempo.minBeatsPerMinute <= beatsPerMinute && beatsPerMinute <= tempo.maxBeatsPerMinute) {
                return tempo.name;
            }
        }
        return null;
    }

    private Tempi() {
        // empty
    }

    private void onCreate() {
        // see: https://de.wikipedia.org/wiki/Tempo_(Musik)
        add("Largo", 40, 60, 60);
        add("Larghetto", 60, 66, 66);
        add("Adagio", 66, 76, 76);
        add("Andante", 76, 108, 90);
        add("Moderato", 108, 120, 110);
        add("Allegro", 120, 168, 140);
        add("Presto", 168, 200, 180);
    }

    private void add(String name, int minBeatsPerMinute, int maxBeatsPerMinute, int defaultBeatsMinute) {
        this.add(new Tempo(name, minBeatsPerMinute, maxBeatsPerMinute, defaultBeatsMinute));
    }
}
