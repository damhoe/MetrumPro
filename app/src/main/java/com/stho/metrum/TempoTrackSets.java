package com.stho.metrum;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;

public class TempoTrackSets {

    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, ArrayList<TempoTrack>> sets = new HashMap<>();

    private void newTrack(int setNumber, String name, int bpm, String dance) {
        TempoTrack track = new TempoTrack(name, bpm, dance);
        ArrayList<TempoTrack> tracks = sets.get(setNumber);
        if (tracks == null) {
            tracks = new ArrayList<TempoTrack>();
            sets.put(setNumber, tracks);
        }
        tracks.add(track);
    }

    public ArrayList<TempoTrack> getTracks(int setNumber) {
        ArrayList<TempoTrack> setList = sets.get(setNumber);
        if (setList != null) {
            return setList;
        } else {
            return new ArrayList<TempoTrack>();
        }
    }

    private TempoTrackSets() {
        // empty
    }

    public static TempoTrackSets create(){
        TempoTrackSets set = new TempoTrackSets();
        set.initialize();
        return set;
    }

    private void initialize() {
        newTrack(1,"The Chicken", 90, "undefined");
        newTrack(1, "Jazz Walz No. 2", 164, "Viennese Waltz");
        newTrack(1,"Dance Tonight", 90, "Discofox");
        newTrack(1,"This is the Life", 200, "Quickstep");
        newTrack(1,"Be my Man", 144, "Foxtrott");
        newTrack(1,"Sunny", 125, "ChaChaCha");
        newTrack(1,"You light up my Life", 85, "Slow Waltz");
        newTrack(1,"Jump, Jive an'Wail", 180, "Jive");
        newTrack(1,"N'Oubliez Jamais", 96, "Rumba");
        newTrack(1,"Fly me to the moon", 80, "Slowfox");
        newTrack(1,"Hot Stuff", 120, "Discofox");
        newTrack(1,"St. James Ballroom", 196, "Swing");

        newTrack(2,"When I get Famous", 128, "ChaChaCha");
        newTrack(2,"Good InTENtions", 174, "Jive");
        newTrack(2,"Another Day in Paradise", 102, "Rumba");
        newTrack(2,"La Bomba", 180, "Salsa");
        newTrack(2,"Tormento", 112, "Slowfox");
        newTrack(2,"Tango", 124, "Tango");
        newTrack(2,"Sexbomb", 126, "Discofox");
        newTrack(2,"Lucky Day", 204, "Quickstep");
        newTrack(2,"Moonriver", 80, "Slow Waltz");
        newTrack(2,"Perhaps, Perhaps, Perhaps", 120, "ChaChaCha");
        newTrack(2,"Perfect Love", 160, "Salsa");
        newTrack(2,"Give me one Reason", 95, "Blues");
        newTrack(2,"Blue Suede Shoes", 190, "Rock'n'Roll");
        newTrack(2,"King of the Road", 100, "Slowfox");

        newTrack(3,"Lets Get Loud", 125, "ChaChaCha");
        newTrack(3,"Love Runs Out", 118, "Discofox");
        newTrack(3,"I Forgot more than you'll ever know about her", 90, "Slow Waltz");
        newTrack(3,"The Pretender", 90, "Foxtrott");
        newTrack(3,"Dirty Dice", 120, "ChaChaCha");
        newTrack(3, "Do the ChaChaCha", 165,"Swing");
        newTrack(3,"Whatever Lola Wants", 124, "Tango");
        newTrack(3,"Lifeboats", 90, "Discofox");
        newTrack(3, "Never Can Tell", 170, "Rock'n'Roll");
        newTrack(3,"Son al Rey", 180, "Salsa");
        newTrack(3,"Sittin on the Dock of the Bay", 100, "Foxtrott");
        newTrack(3,"Great Balls of Fire", 172, "Rock'n'Roll");
        newTrack(3,"Misery", 96, "Slow Waltz");
        newTrack(3,"Disko Partizani", 100, "Discofox");
        newTrack(3,"Schüttel deinen Speck", 108, "undefined");

        newTrack(4,"Deine Zeit", 73, "undefined");
        newTrack(4,"R-O-C-K", 173, "Jive");
        newTrack(4, "Gimme! Gimme! Gimme!", 100, "Discofox");
        newTrack(4, "Zuckerkönig", 200, "Salsa");
        newTrack(4,"Love ain't here anymore", 90, "Slow Waltz");
        newTrack(4,"Billie Jean", 116, "Discofox");
        newTrack(4,"Sail along silvery Moon", 95, "Foxtrott");
        newTrack(4,"Je Veux", 150, "Jive");
        newTrack(4,"Fire it up", 115, "Foxtrott");
        newTrack(4,"Booty Swing", 114, "undefined");
        newTrack(4,"Shake, Rattle and Roll", 176, "Jive");
        newTrack(4,"Everybody", 110, "undefined");
        newTrack(4,"Mambo No.5", 90, "Discofox");
        newTrack(4,"Lila Wolken", 146, "undefined");
        newTrack(4, "Waterloo", 158, "Jive");
        newTrack(4, "99 Luftballons", 186, "Discofox");
        newTrack(4, "Live is Life", 93, "Foxtrott");

        newTrack(5,"Rock around the Clock", 186, "Rock'n'Roll");
        newTrack(5,"Havana", 95, "Rumba");
        newTrack(5,"Masterpiece", 115, "Rumba");
        newTrack(5,"God put a smile upon your Face", 125, "ChaChaCha");
        newTrack(5,"All that she wants", 95, "Discofox");
        newTrack(5,"Stand By Me", 122, "ChaChaCha");
        newTrack(5,"Ghost Town", 90, "Discofox");
    }
}
