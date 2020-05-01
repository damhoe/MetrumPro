package com.stho.metrum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TempoTrackListActivity extends Activity {

    private ArrayList<TempoTrack> tempoTrackList = new ArrayList<TempoTrack>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempo_of_sets);

        int currentSetNumber = getIntent().getIntExtra(MainActivity.INTENT_KEY_SET, 1);
        initialize(currentSetNumber);

        TextView textView = (TextView) findViewById(R.id.label_captionTrackList);
        textView.setText("Set " + Integer.toString(currentSetNumber));
        textView.setTextSize(30);
        ArrayAdapter<TempoTrack> trackListAdapter = new ArrayAdapter<TempoTrack>(this, R.layout.list_item, R.id.textViewListItem, tempoTrackList);
        ListView trackListView = findViewById(R.id.listViewSetList);
        trackListView.setAdapter(trackListAdapter);
        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TempoTrack currentTempoTrack = tempoTrackList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, currentTempoTrack.getBeatsPerMinute());
                    intent.putExtra(MainActivity.INTENT_KEY_TRACK_NAME, currentTempoTrack.getName());
                    intent.putExtra(MainActivity.INTENT_KEY_DANCE, currentTempoTrack.getDance());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                catch (Exception e){}
            }
        });
    }

    private void initialize(int setNumber) {
        switch (setNumber) {
            case 1:
                tempoTrackList.add(new TempoTrack("The Chicken", 164, "undefined"));
                tempoTrackList.add(new TempoTrack("Jazz Walz No. 2", 164, "Viennese Waltz"));
                tempoTrackList.add(new TempoTrack("Love Runs Out", 118, "Discofox"));
                tempoTrackList.add(new TempoTrack("Dirty Dice", 120, "ChaChaCha"));
                tempoTrackList.add(new TempoTrack("Lucky Day", 204, "Quickstep"));
                tempoTrackList.add(new TempoTrack("Jump, Jive an'Wail", 180, "Jive"));
                tempoTrackList.add(new TempoTrack("Fly me to the moon", 80, "Slowfox"));
                tempoTrackList.add(new TempoTrack("Sexbomb", 126, "Discofox"));
                tempoTrackList.add(new TempoTrack("N'Oubliez Jamais", 96, "Rumba"));
                tempoTrackList.add(new TempoTrack("St. James Ballroom", 196, "Swing"));
                tempoTrackList.add(new TempoTrack("Moonriver", 80, "Slow Waltz"));
                tempoTrackList.add(new TempoTrack("Son al Rey", 180, "Salsa"));
                tempoTrackList.add(new TempoTrack("Blue Suede Shoes", 190, "Rock'n'Roll"));
                break;
            case 2:
                tempoTrackList.add(new TempoTrack("When I get Famous", 128, "ChaChaCha"));
                tempoTrackList.add(new TempoTrack("The Pretender", 90, "Foxtrott"));
                tempoTrackList.add(new TempoTrack("Good InTENtions", 174, "Jive"));
                tempoTrackList.add(new TempoTrack("King of the Road", 100, "Slowfox"));
                tempoTrackList.add(new TempoTrack("Misery", 96, "Slow Waltz"));
                tempoTrackList.add(new TempoTrack("R-O-C-K", 173, "Jive"));
                tempoTrackList.add(new TempoTrack("Give me one Reason", 95, "Blues"));
                tempoTrackList.add(new TempoTrack("Perhaps, Perhaps, Perhaps", 120, "ChaChaCha"));
                tempoTrackList.add(new TempoTrack("Great Balls of Fire", 172, "Rock'n'Roll"));
                tempoTrackList.add(new TempoTrack("Lifeboats", 90, "Discofox"));
                tempoTrackList.add(new TempoTrack("Another Day in Paradise", 102, "Rumba"));
                tempoTrackList.add(new TempoTrack("Be my Man", 140, "Foxtrott"));
                tempoTrackList.add(new TempoTrack("Deine Zeit", 73, "undefined"));
                tempoTrackList.add(new TempoTrack("Booty Swing", 114, "undefined"));
                break;
            case 3:
                tempoTrackList.add(new TempoTrack("Hot Stuff", 120, "Discofox"));
                tempoTrackList.add(new TempoTrack("Lets Get Loud", 125, "ChaChaCha"));
                tempoTrackList.add(new TempoTrack("Rock around the Clock", 186, "Rock'n'Roll"));
                tempoTrackList.add(new TempoTrack("I Forgot more than you'll ever know about her", 90, "Slow Waltz"));
                tempoTrackList.add(new TempoTrack("Mambo No.5", 90, "Discofox"));
                tempoTrackList.add(new TempoTrack("La Bomba", 180, "Salsa"));
                tempoTrackList.add(new TempoTrack("Billie Jean", 116, "Discofox"));
                tempoTrackList.add(new TempoTrack("Havana", 95, "Rumba"));
                tempoTrackList.add(new TempoTrack("Schüttel deinen Speck", 108, "undefined"));
                tempoTrackList.add(new TempoTrack("Everybody", 110, "undefined"));
                tempoTrackList.add(new TempoTrack("Fire it up", 115, "Foxtrott"));
                tempoTrackList.add(new TempoTrack("Lila Wolken", 146, "undefined"));
                break;
            case 4:
                tempoTrackList.add(new TempoTrack("Masterpiece", 115, "Rumba"));
                tempoTrackList.add(new TempoTrack("Love ain't here anymore", 90, "Slow Waltz"));
                tempoTrackList.add(new TempoTrack("Sail along silvery Moon", 95, "Foxtrott"));
                tempoTrackList.add(new TempoTrack("Je Veux", 150, "Jive"));
                tempoTrackList.add(new TempoTrack("Shake, Rattle and Roll", 176, "Jive"));
                tempoTrackList.add(new TempoTrack("God put a smile upon your Face", 125, "ChaChaCha"));
                tempoTrackList.add(new TempoTrack("All that she wants", 95, "Discofox"));
                tempoTrackList.add(new TempoTrack("Whatever Lola Wants", 124, "Tango"));
                tempoTrackList.add(new TempoTrack("Sittin on the Dock of the Bay", 100, "Foxtrott"));
                tempoTrackList.add(new TempoTrack("Stand By Me", 122, "ChaChaCha"));
                tempoTrackList.add(new TempoTrack("Disko Partizani", 100, "Discofox"));
                tempoTrackList.add(new TempoTrack("Tormento", 112, "Slowfox"));
                tempoTrackList.add(new TempoTrack("Tango", 124, "Tango"));
                tempoTrackList.add(new TempoTrack("Perfect Love", 160, "Salsa"));
                tempoTrackList.add(new TempoTrack("Dance Tonight", 90, "Discofox"));
                tempoTrackList.add(new TempoTrack("This is the Life", 200, "Quickstep"));
                tempoTrackList.add(new TempoTrack("You light up my Life", 85, "Slow Waltz"));
                tempoTrackList.add(new TempoTrack("Ghost Town", 90, "Discofox"));
                break;
        }
    }
}
