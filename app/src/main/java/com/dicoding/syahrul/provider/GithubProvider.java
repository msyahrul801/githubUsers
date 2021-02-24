package com.dicoding.syahrul.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dicoding.syahrul.db.DBHelper;

import java.util.Objects;

import static com.dicoding.syahrul.db.DatabaseContract.AUTHORITY;
import static com.dicoding.syahrul.db.DatabaseContract.UsersColumns.CONTENT_URI;
import static com.dicoding.syahrul.db.DatabaseContract.UsersColumns.TABLE_NAME;

public class GithubProvider extends ContentProvider {
    private static final int USERS = 1;
    private DBHelper dbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USERS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        dbHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        if (sUriMatcher.match(uri) == USERS) {
            cursor = dbHelper.queryAll();
        } else {
            cursor = null;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;
        if (sUriMatcher.match(uri) == USERS) {
            added = dbHelper.insert(contentValues);
        } else {
            added = 0;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated;
        if (sUriMatcher.match(uri) == USERS) {
            updated = dbHelper.update(uri.getLastPathSegment(), contentValues);
        } else {
            updated = 0;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        deleted = dbHelper.deleteByUsername(uri.getLastPathSegment());
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }
}