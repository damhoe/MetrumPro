package com.stho.metrum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TempoTrackDataSource implements IDatabaseListener {

    private SQLiteDatabase database;
    private TempoTrackDbHelper dbHelper;

    private String[] columns = {
        TempoTrackDbHelper.COLUMN_ID,
        TempoTrackDbHelper.COLUMN_NAME,
        TempoTrackDbHelper.COLUMN_DANCE,
        TempoTrackDbHelper.COLUMN_BPM,
    };

    // Singleton pattern
    private static TempoTrackDataSource data = null;

    public static ArrayList<TempoTrack> getData(Context context) {

        ArrayList<TempoTrack> tracks = null;

        if (data == null) {
            data = new TempoTrackDataSource(context);
            try {
                data.open();
                tracks = data.getAllTempoTracks();
            }
            finally {
                data.close();
            }
        }

        return tracks;
    }

    private TempoTrackDataSource(Context context) {
        dbHelper = new TempoTrackDbHelper(context);
        //initialize();
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    void putTempoTrack(TempoTrack tempoTrack) {
        try {
            ContentValues values = new ContentValues();
            values.put(TempoTrackDbHelper.COLUMN_BPM, tempoTrack.getBeatsPerMinute());
            values.put(TempoTrackDbHelper.COLUMN_NAME, tempoTrack.getName());
            values.put(TempoTrackDbHelper.COLUMN_DANCE, tempoTrack.getDance());

            long insertId = database.insert(TempoTrackDbHelper.TABLE_TEMPO_TRACKS, null, values);

            Cursor cursor = database.query(TempoTrackDbHelper.TABLE_TEMPO_TRACKS, columns, TempoTrackDbHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
            cursor.close();

        }
        catch (Exception e) {
            Log.v("ERROR: ", e.getMessage());
        }
    }

    public TempoTrack cursorToTempoTrack(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(TempoTrackDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(TempoTrackDbHelper.COLUMN_NAME);
        int idDance = cursor.getColumnIndex(TempoTrackDbHelper.COLUMN_DANCE);
        int idBpm = cursor.getColumnIndex(TempoTrackDbHelper.COLUMN_BPM);

        String name = cursor.getString(idName);
        String dance = cursor.getString(idDance);
        int bpm = cursor.getInt(idBpm);

        return new TempoTrack(name, bpm, dance);
    }

    public ArrayList<TempoTrack> getAllTempoTracks() {
        ArrayList<TempoTrack> tempoTracks = new ArrayList<>();

        Cursor cursor  = database.query(TempoTrackDbHelper.TABLE_TEMPO_TRACKS, columns, null, null, null, null, null);

        cursor.moveToFirst();
        TempoTrack tempoTrack;

        while (!cursor.isAfterLast()) {
            tempoTrack = cursorToTempoTrack(cursor);
            tempoTracks.add(tempoTrack);
            cursor.moveToNext();
        }

        cursor.close();

        return tempoTracks;
    }

    private void newTempoTrack(String name, int bpm, String dance) {
        putTempoTrack(new TempoTrack(name, bpm, dance));
    }

    @Override
    public void onNotifyDatabaseCreation() {

    }
}
