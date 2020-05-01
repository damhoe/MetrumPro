package com.stho.metrum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class DefineFrequencyActivity extends Activity {

    private class ViewHolder
    {
        RadioGroup radioButtons;
        RadioButton radioButtonFree;
        EditText editFree;
        Button button;
    }

    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_frequency);

        final int beatsPerMinute = getIntent().getIntExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, 100);

        // see: https://de.wikipedia.org/wiki/Tempo_(Musik)
        viewHolder = new ViewHolder();
        viewHolder.radioButtons = (RadioGroup) findViewById(R.id.radioButtons);
        viewHolder.radioButtons.setOrientation(RadioGroup.VERTICAL);
        viewHolder.radioButtons.removeAllViews();
        viewHolder.editFree = (EditText) findViewById(R.id.editFree);
        viewHolder.button = (Button) findViewById(R.id.button);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    int beatsPerMinute = getBeatsPerMinute();
                    intent.putExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, beatsPerMinute);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                catch (Exception ex) {
                    // do nothing
                }
            }
        });

        for (Tempo tempo : Tempi.getInstance()) {
            createRadioButton(tempo.name, tempo.defaultBeatsMinute);
        }

        createRadioButtonFree(beatsPerMinute);
        setBeatsPerMinute(beatsPerMinute);
    }

    private void createRadioButton(String name, final int frequency) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(name);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewHolder.editFree.setText(String.format(Locale.ENGLISH, "%d", frequency));
                    viewHolder.editFree.setEnabled(false);
                }
            }
        });
        viewHolder.radioButtons.addView(radioButton);
    }

    private void createRadioButtonFree(final int frequency) {
        viewHolder.radioButtonFree = new RadioButton(this);
        viewHolder.radioButtonFree.setText(R.string.label_free);
        viewHolder.radioButtonFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewHolder.editFree.setEnabled(true);
                    viewHolder.editFree.setText(String.format(Locale.ENGLISH, "%d", frequency));
                }
            }
        });
        viewHolder.radioButtons.addView(viewHolder.radioButtonFree);
    }

    private int getBeatsPerMinute() throws Exception {
        String text = viewHolder.editFree.getText().toString();
        return Integer.parseInt(text);
    }

    private void setBeatsPerMinute(int beatsPerMinute) {
        viewHolder.editFree.setText(String.format(Locale.ENGLISH, "%d", beatsPerMinute));

        int count = viewHolder.radioButtons.getChildCount();
        for (int i = 0; i < count; i++)
        {
            RadioButton radioButton = (RadioButton)viewHolder.radioButtons.getChildAt(i);
            String name = radioButton.getText().toString();
            if (Tempi.getDefaultBeatsByName(name) == beatsPerMinute) {
                radioButton.setChecked(true);
                viewHolder.editFree.setEnabled(false);
                return;
            }
        }

        viewHolder.radioButtonFree.setChecked(true);
        viewHolder.editFree.setEnabled(true);
    }
}
