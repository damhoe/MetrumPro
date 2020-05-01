package com.stho.metrum;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/*
  Activity to specify the combination of sounds.
  Currently:
    - a vertical list control
    - in each list entry:
            checkboxes for all 4 possible beats
            delete button to remove the entry
            an icon to visualize the resulting beat
    - buttons to add, save

  TODO: icon should really visualize a combination of 4 basic features or nothing
  TODO: horizontal list contral (through a table?)
  TODO: slider control to remove

  OPEN:
    - save into static structure or into settings
 */
public class DefineBeatsActivity extends ListActivity {

    final ViewModel viewModel = new ViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_beats);
        viewModel.fromIntent(getIntent());

        // see: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterperformance
        final ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(DefineBeatsActivity.this, R.layout.activity_define_beats_list_item, viewModel.getBeats()) {

            class ViewHolder {
                TextView position;
                ImageView icon;
                CheckBox tick;
                CheckBox tock;
                CheckBox bass;
                CheckBox cymbal;
                CheckBox crack;
                ImageView delete;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewHolder viewHolder = null;

                View rowView = convertView;
                if (rowView == null) {
                    LayoutInflater inflater = DefineBeatsActivity.this.getLayoutInflater();
                    rowView = inflater.inflate(R.layout.activity_define_beats_list_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.position = rowView.findViewById(R.id.position);
                    viewHolder.icon = rowView.findViewById(R.id.icon);
                    viewHolder.tick = rowView.findViewById(R.id.tick);
                    viewHolder.tock = rowView.findViewById(R.id.tock);
                    viewHolder.bass = rowView.findViewById(R.id.bass);
                    viewHolder.cymbal = rowView.findViewById(R.id.cymbal);
                    viewHolder.crack = rowView.findViewById(R.id.crack);
                    viewHolder.delete = rowView.findViewById(R.id.delete_button);
                    updateRowView(position, viewHolder);
                    setListener(viewHolder);
                    rowView.setTag(viewHolder);
                }
                else {
                    viewHolder = (ViewHolder)rowView.getTag();
                    updateRowView(position, viewHolder);
                }
                return rowView;
            }

            private void setListener(final  ViewHolder viewHolder) {
                viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playBeat(viewHolder);
                    }
                });
                viewHolder.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        updateBeat(viewHolder, ViewModel.TICK, b);
                    }
                });
                viewHolder.tock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        updateBeat(viewHolder, ViewModel.TOCK, b);
                    }
                });
                viewHolder.bass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        updateBeat(viewHolder, ViewModel.BASS, b);
                    }
                });
                viewHolder.cymbal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        updateBeat(viewHolder, ViewModel.CYMBAL, b);
                    }
                });
                viewHolder.crack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        updateBeat(viewHolder, ViewModel.CRACK, b);
                    }
                });
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int position = (int)viewHolder.position.getTag();
                        viewModel.deleteBeat(position);
                    }
                });
            }

            private void updateBeat(final ViewHolder viewHolder, final int value, boolean b) {
                final int position = (int)viewHolder.position.getTag();
                updateBeat(position, value, b);
            }

            private void updateBeat(final int position, final int value, boolean b) {
                if (b) {
                    viewModel.addBeat(position, value);
                } else {
                    viewModel.removeBeat(position, value);
               }
            }

            private void playBeat(final ViewHolder viewHolder) {
                final int position = (int)viewHolder.position.getTag();
                playBeat(position);
            }

            private void playBeat(final int position) {
                int key = viewModel.getBeat(position);
                int samples = viewModel.getNumberSamplesPerSplittedBeat() / 2; // somewhat smaller!
                MetrumAudioPlayer player = null;
                try {
                    player = new MetrumAudioPlayer(getContext());
                    player.prepareAudio();
                    player.play(key, samples);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
                }
            }

            private void toast(final String text) {
                Toast toast = Toast.makeText(getContext(), text, 5);
                toast.show();
            }

            private void updateRowView(final int position, final ViewHolder viewHolder) {
                final int beat = viewModel.getBeat(position);
                viewHolder.position.setText(String.format(Locale.ENGLISH, "%d.", position+1));
                viewHolder.position.setTag(position);
                viewHolder.tick.setChecked((beat & ViewModel.TICK) == ViewModel.TICK);
                viewHolder.tock.setChecked((beat & ViewModel.TOCK) == ViewModel.TOCK);
                viewHolder.bass.setChecked((beat & ViewModel.BASS) == ViewModel.BASS);
                viewHolder.cymbal.setChecked((beat & ViewModel.CYMBAL) == ViewModel.CYMBAL);
                viewHolder.crack.setChecked((beat & ViewModel.CRACK) == ViewModel.CRACK);
                viewHolder.bass.setChecked((beat & ViewModel.BASS) == ViewModel.BASS);
                viewHolder.icon.setImageResource(ViewModel.getImageFor(beat));
            }
        };
        setListAdapter(adapter);
        viewModel.setOnChangeListener(new ViewModel.OnChangeListener() {
            @Override
            public void onChange() {
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addBeat();
                getListView().setSelection(viewModel.size() - 1);
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setResult(RESULT_OK, viewModel.getIntent());
                    finish();
                }
                catch (Exception ex) {
                    // do nothing
                }
            }
        });
        findViewById(R.id.salsa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setSalsa();
            }
        });
        findViewById(R.id.chacha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setChaCha();
            }
        });
        findViewById(R.id.slowfox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setSlowFox();
            }
        });
        findViewById(R.id.waltz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setWaltz();
            }
        });
        findViewById(R.id.metrum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setMetrum();
            }
        });
    }
}
