package com.dicoding.syahrul.helper;

import android.database.Cursor;

import com.dicoding.syahrul.Entity.UserEn;
import com.dicoding.syahrul.db.DatabaseContract;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<UserEn> mapCursorToArrayList(Cursor usersCursor) {
        ArrayList<UserEn> userList = new ArrayList<>();

        while (usersCursor.moveToNext()) {
            int id = usersCursor.getInt(usersCursor.getColumnIndexOrThrow(DatabaseContract.UsersColumns._ID));
            String title = usersCursor.getString(usersCursor.getColumnIndexOrThrow(DatabaseContract.UsersColumns.USERNAME));
            String description = usersCursor.getString(usersCursor.getColumnIndexOrThrow(DatabaseContract.UsersColumns.AVATAR));
            userList.add(new UserEn(id, title, description));
        }

        return userList;
    }

    public static UserEn mapCursorToObject(Cursor usersCursor) {
        usersCursor.moveToFirst();
        int id = usersCursor.getInt(usersCursor.getColumnIndexOrThrow(DatabaseContract.UsersColumns._ID));
        String title = usersCursor.getString(usersCursor.getColumnIndexOrThrow(DatabaseContract.UsersColumns.USERNAME));
        String description = usersCursor.getString(usersCursor.getColumnIndexOrThrow(DatabaseContract.UsersColumns.AVATAR));

        return new UserEn(id, title, description);
    }
}
