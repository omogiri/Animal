package com.example.latihan_praktikum_9.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latihan_praktikum_9.data.entity.Users;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.CountryEntry.TABLE_NAME + " (" +
                    DatabaseContract.CountryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.CountryEntry.COLUMN_NAME + " TEXT, " +
                    DatabaseContract.CountryEntry.COLUMN_USERNAME + " TEXT," +
                    DatabaseContract.CountryEntry.COLUMN_EMAIL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.CountryEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public List<Users> searchUsers(String keyword) {
        List<Users> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = DatabaseContract.CountryEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyword + "%"};

        Cursor cursor = db.query(
                DatabaseContract.CountryEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get data from the cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CountryEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CountryEntry.COLUMN_NAME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CountryEntry.COLUMN_USERNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CountryEntry.COLUMN_EMAIL));
                // Create animal with the data
                Users animal = new Users(id, name, username, email);
                result.add(animal);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }
}
