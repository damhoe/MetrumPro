package com.stho.metrum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TempoTrackAdapter extends BaseAdapter {

    private final ArrayList<TempoTrack> tempoTracks;
    private final Context mContext;

    public TempoTrackAdapter(Context context, ArrayList<TempoTrack> tempoTracks) {
        this.tempoTracks = tempoTracks;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return tempoTracks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_item, null);
        }

        assert convertView != null;
        TextView tempoTxtView = (TextView) convertView.findViewById(R.id.tempo_view_grid);
        TextView nameTxtView = (TextView) convertView.findViewById(R.id.track_name_view_grid);
        TextView danceTxtView = (TextView) convertView.findViewById(R.id.dance_view_grid);

        tempoTxtView.setText(Integer.toString(tempoTracks.get(position).getBeatsPerMinute()));
        nameTxtView.setText(tempoTracks.get(position).getName());
        danceTxtView.setText(tempoTracks.get(position).getDance());

        //TextView textView = new TextView(mContext);
        //textView.setText(tempoTracks.get(position).toString());
        //textView.setTextColor(mContext.getResources().getColor(R.color.colorControlHighlight));
        //textView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));

        return convertView;
    }
}
