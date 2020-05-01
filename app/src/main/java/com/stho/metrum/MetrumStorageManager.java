package com.stho.metrum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/*
 https://developer.android.com/training/data-storage/sqlite.html#WriteDbRow
 */
public class MetrumStorageManager {

    private final StorageDatabaseHelper dbHelper;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    MetrumStorageManager(Context context) {
        dbHelper = new StorageDatabaseHelper(context);
    }

    void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    /* Inner class that defines the table contents */
    public static class StorageConfiguration implements BaseColumns {
        static final String TABLE_NAME = "entry";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_BEATS_PER_MINUTE = "beats_per_minute";
        static final String COLUMN_NAME_BEATS_SPLIT = "sounds_per_beat";
        static final String COLUMN_NAME_BEATS = "beats";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StorageConfiguration.TABLE_NAME + " (" +
                    StorageConfiguration._ID + " INTEGER PRIMARY KEY," +
                    StorageConfiguration.COLUMN_NAME_TITLE + " TEXT," +
                    StorageConfiguration.COLUMN_NAME_DATE + " DATETIME," +
                    StorageConfiguration.COLUMN_NAME_BEATS_PER_MINUTE + " INTEGER," +
                    StorageConfiguration.COLUMN_NAME_BEATS_SPLIT + " INTEGER," +
                    StorageConfiguration.COLUMN_NAME_BEATS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StorageConfiguration.TABLE_NAME;

    private static final String SQL_QUERY_ENTRIES =
            "SELECT " +
                    StorageConfiguration._ID + ", " +
                    StorageConfiguration.COLUMN_NAME_TITLE + ", " +
                    StorageConfiguration.COLUMN_NAME_DATE + ", " +
                    StorageConfiguration.COLUMN_NAME_BEATS_PER_MINUTE + ", " +
                    StorageConfiguration.COLUMN_NAME_BEATS_SPLIT + ", " +
                    StorageConfiguration.COLUMN_NAME_BEATS +
                    " FROM " + StorageConfiguration.TABLE_NAME +
                    " ORDER BY " + StorageConfiguration.COLUMN_NAME_TITLE;

    private static final String SQL_QUERY_ENTRY_BY_TITLE =
            "SELECT " +
                    StorageConfiguration._ID + ", " +
                    StorageConfiguration.COLUMN_NAME_TITLE + ", " +
                    StorageConfiguration.COLUMN_NAME_DATE + ", " +
                    StorageConfiguration.COLUMN_NAME_BEATS_PER_MINUTE + ", " +
                    StorageConfiguration.COLUMN_NAME_BEATS_SPLIT + ", " +
                    StorageConfiguration.COLUMN_NAME_BEATS +
                    " FROM " + StorageConfiguration.TABLE_NAME +
                    " WHERE " + StorageConfiguration.COLUMN_NAME_TITLE + "=?" +
                    " ORDER BY " + StorageConfiguration.COLUMN_NAME_TITLE;

    class StorageDatabaseHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        static final int DATABASE_VERSION = 2;
        static final String DATABASE_NAME = "metrum.db";

        StorageDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
            createDefaultEntries();
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private void createDefaultEntries() {
        save("SALSA", Calendar.getInstance().getTime(), 100, 1, 2, 1, 0);
        save("SLOW WALTZ", Calendar.getInstance().getTime(), 80, 1, 2, 2);
        save("DISCO FOX", Calendar.getInstance().getTime(), 110, 1, 2, 1, 2);
    }

    void save(String title, Date date, int beatsPerMinute, int beatsSplit, Integer ... beats) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, beats);
        save(title, date, beatsPerMinute, beatsSplit, list);
    }

    void save(String title, Date date, int beatsPerMinute, int beatsSplit, ArrayList<Integer> beats) {

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StorageConfiguration.COLUMN_NAME_TITLE, title);
        values.put(StorageConfiguration.COLUMN_NAME_DATE, MetrumStorageManager.toString(date));
        values.put(StorageConfiguration.COLUMN_NAME_BEATS_PER_MINUTE, beatsPerMinute);
        values.put(StorageConfiguration.COLUMN_NAME_BEATS_SPLIT, beatsSplit);
        values.put(StorageConfiguration.COLUMN_NAME_BEATS, MetrumStorageManager.toString(beats));

        // Insert the new row, returning the primary key value of the new row
        db.insert(StorageConfiguration.TABLE_NAME, null, values);
    }

    void reset() {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);

        createDefaultEntries();
    }

    Cursor read() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(SQL_QUERY_ENTRIES, null);
    }

    Cursor read(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(SQL_QUERY_ENTRY_BY_TITLE, new String[] { title });
    }

    private static String toString(Date date) {
        return dateFormat.format(date);
    }

    private static Date toDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return Calendar.getInstance().getTime();
        }
    }

    private static String toString(ArrayList<Integer> beats) {
        StringBuilder sb = new StringBuilder();
        for (int beat : beats) {
            sb.append(String.format(Locale.ENGLISH, "%d", beat));
            sb.append(";");
        }
        return sb.toString();
    }

    private static ArrayList<Integer> toArrayList(String beatsString) {
        String[] items = beatsString.split(";");
        ArrayList<Integer> beats = new ArrayList<>();
        for (String item : items) {
            beats.add(Integer.parseInt(item));
        }
        return beats;
    }

    private static int[] toArray(String beatsString) {
        String[] items = beatsString.split(";");
        int[] array = new int[items.length];
        for (int position = 0; position < array.length; position++) {
            array[position] = Integer.parseInt(items[position]);
        }
        return array;
    }

    static String getTitle(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(StorageConfiguration.COLUMN_NAME_TITLE);
        return cursor.getString(columnIndex);
    }

    static Date getDate(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(StorageConfiguration.COLUMN_NAME_DATE);
        return toDate(cursor.getString(columnIndex));
    }

    static int getBeatsPerMinute(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(StorageConfiguration.COLUMN_NAME_BEATS_PER_MINUTE);
        return cursor.getInt(columnIndex);
    }

    static int getBeatsSplit(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(StorageConfiguration.COLUMN_NAME_BEATS_SPLIT);
        return cursor.getInt(columnIndex);
    }

    static ArrayList<Integer> getBeats(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(StorageConfiguration.COLUMN_NAME_BEATS);
        return toArrayList(cursor.getString(columnIndex));
    }

    static int[] getBeatsArray(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(StorageConfiguration.COLUMN_NAME_BEATS);
        return toArray(cursor.getString(columnIndex));
    }
}
