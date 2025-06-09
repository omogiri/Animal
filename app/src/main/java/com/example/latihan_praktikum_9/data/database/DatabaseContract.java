package com.example.latihan_praktikum_9.data.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private DatabaseContract() {}

    public static class CountryEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
    }
}
