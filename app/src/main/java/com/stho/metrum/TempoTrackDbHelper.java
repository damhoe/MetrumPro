package com.stho.metrum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TempoTrackDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG  = TempoTrackDbHelper.class.getSimpleName();

    public static final String DB_NAME = "tempo_track.db";
    public static final double DB_VERSION = 1.0;

    public static final String TABLE_TEMPO_TRACKS = "table_tempo_tracks";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BPM = "bpm";
    public static final String COLUMN_DANCE = "dance";
    public static final String COLUMN_SETNUM = "setnumber"; // this is useful for the second database
    // where the sets are saved

    public static final String SQL_CREATE_STRING =
            "CREATE TABLE " + TABLE_TEMPO_TRACKS +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BPM + " INTEGER NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DANCE + " TEXT NOT NULL);";

    public TempoTrackDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    private String[] columns = {
            TempoTrackDbHelper.COLUMN_ID,
            TempoTrackDbHelper.COLUMN_NAME,
            TempoTrackDbHelper.COLUMN_DANCE,
            TempoTrackDbHelper.COLUMN_BPM,
    };
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_STRING);
            initialize(db);
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error while creating the table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void newTempoTrack(SQLiteDatabase database, String name, int bpm, String dance) {
        ContentValues values = new ContentValues();
        values.put(TempoTrackDbHelper.COLUMN_BPM, bpm);
        values.put(TempoTrackDbHelper.COLUMN_NAME, name);
        values.put(TempoTrackDbHelper.COLUMN_DANCE, dance);

        long insertId = database.insert(TempoTrackDbHelper.TABLE_TEMPO_TRACKS, null, values);

        Cursor cursor = database.query(TempoTrackDbHelper.TABLE_TEMPO_TRACKS, columns, TempoTrackDbHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.close();

    }

    private void initialize(SQLiteDatabase db) {
        newTempoTrack(db, "The Chicken", 90, "undefined");
        newTempoTrack(db, "Jazz Walz No. 2", 164, "Viennese Waltz");
        newTempoTrack(db, "Love Runs Out", 118, "Discofox");
        newTempoTrack(db, "Dirty Dice", 120, "ChaChaCha");
        newTempoTrack(db, "Lucky Day", 204, "Quickstep");
        newTempoTrack(db, "Jump, Jive an'Wail", 180, "Jive");
        newTempoTrack(db, "Fly me to the moon", 80, "Slowfox");
        newTempoTrack(db, "Sexbomb", 126, "Discofox");
        newTempoTrack(db, "N'Oubliez Jamais", 96, "Rumba");
        newTempoTrack(db, "St. James Ballroom", 196, "Swing");
        newTempoTrack(db, "Moonriver", 80, "Slow Waltz");
        newTempoTrack(db, "Son al Rey", 180, "Salsa");
    }
}
