package com.stho.metrum;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class StorageActivity extends ListActivity {

    final private ViewModel viewModel = new ViewModel();
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private class ViewHolder {
        TextView title;
        TextView date;
        ImageView delete;
    }

    /*
        References:
            https://guides.codepath.com/android/Populating-a-ListView-with-a-CursorAdapter
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        viewModel.fromIntent(getIntent());

        final MetrumStorageManager storage = new MetrumStorageManager(this);
        try {
            final Cursor cursor = storage.read();
            final CursorAdapter adapter = new CursorAdapter(this, cursor) {

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    View view = LayoutInflater.from(context).inflate(R.layout.storage_list_item, parent, false);
                    ViewHolder viewHolder = new ViewHolder();
                    viewHolder.title = view.findViewById(R.id.title);
                    viewHolder.date = view.findViewById(R.id.date);
                    viewHolder.delete = view.findViewById(R.id.delete);
                    view.setTag(viewHolder);
                    return view;
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    viewHolder.title.setText(MetrumStorageManager.getTitle(cursor));
                    viewHolder.date.setText(dateFormat.format(MetrumStorageManager.getDate(cursor)));
                }
            };
            setListAdapter(adapter);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ViewHolder viewHolder = (ViewHolder)view.getTag();
                    if (viewHolder != null) {
                        onChoose(viewHolder);
                    }
                }
            });
            findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onReset();
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            storage.close();
        }
    }

    private void onChoose(ViewHolder viewHolder) {
        final MetrumStorageManager storage = new MetrumStorageManager(StorageActivity.this);
        try {
            String title = viewHolder.title.getText().toString();
            Cursor cursor = storage.read(title);
            if (cursor.moveToFirst()) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.INTENT_KEY_TITLE, MetrumStorageManager.getTitle(cursor));
                intent.putExtra(MainActivity.INTENT_KEY_BEATS_PER_MINUTE, MetrumStorageManager.getBeatsPerMinute(cursor));
                intent.putExtra(MainActivity.INTENT_KEY_BEATS_SPLIT, MetrumStorageManager.getBeatsSplit(cursor));
                intent.putExtra(MainActivity.INTENT_KEY_BEATS, MetrumStorageManager.getBeatsArray(cursor));
                setResult(RESULT_OK, intent);
                finish();
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            storage.close();
        }
    }

    private void onReset() {
        final MetrumStorageManager storage = new MetrumStorageManager(StorageActivity.this);
        try {
            storage.reset();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            storage.close();
        }

    }
}
