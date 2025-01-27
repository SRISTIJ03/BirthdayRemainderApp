package com.sristi.birthdayreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 2; // Incremented the version for schema update

    public static final String TABLE_REMINDERS = "reminders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date"; // Added date column
    public static final String COLUMN_TIME = "time"; // Added time column

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_REMINDERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " + // Added date column
                COLUMN_TIME + " TEXT" + // Added time column
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the date column if upgrading from version 1 to version 2
            String alterTable = "ALTER TABLE " + TABLE_REMINDERS + " ADD COLUMN " + COLUMN_DATE + " TEXT";
            db.execSQL(alterTable);
        }
    }

    // Method to add a reminder
    public void addReminder(String title, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date); // Insert date
        values.put(COLUMN_TIME, time); // Insert time
        db.insert(TABLE_REMINDERS, null, values);
        db.close();
    }

    // Method to update a reminder
    public void updateReminder(int id, String title, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date); // Update date
        values.put(COLUMN_TIME, time); // Update time
        db.update(TABLE_REMINDERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to get all reminders
    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)); // Retrieve date
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)); // Retrieve time
                reminders.add(new Reminder(id, title, date, time)); // Pass date and time
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reminders;
    }

    // Method to delete a reminder
    public void deleteReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
