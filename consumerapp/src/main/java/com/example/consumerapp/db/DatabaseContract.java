package com.example.consumerapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String AUTHORITY = "com.dicoding.syahrul";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class UsersColumns implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String USERNAME = "username";
        public static final String AVATAR = "avatar";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}