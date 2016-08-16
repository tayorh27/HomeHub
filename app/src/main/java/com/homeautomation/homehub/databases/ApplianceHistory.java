package com.homeautomation.homehub.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.homeautomation.homehub.information.History;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 04-Aug-16.
 */
public class ApplianceHistory {

    ApplianceHistoryHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public ApplianceHistory(Context context) {
        helper = new ApplianceHistoryHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertMyPost(ArrayList<History> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + ApplianceHistoryHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            History current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.history);
            statement.bindString(3, current.dateTime);
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<History> getAllMyPosts() {
        ArrayList<History> currentData = new ArrayList<>();

        String[] columns = {
                ApplianceHistoryHelper.COLUMN_ID,
                ApplianceHistoryHelper.COLUMN_HISTORY,
                ApplianceHistoryHelper.COLUMN_DATETIME
        };
        Cursor cursor = sqLiteDatabase.query(ApplianceHistoryHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                History current = new History();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ApplianceHistoryHelper.COLUMN_ID)));
                current.history = cursor.getString(cursor.getColumnIndex(ApplianceHistoryHelper.COLUMN_HISTORY));
                current.dateTime = cursor.getString(cursor.getColumnIndex(ApplianceHistoryHelper.COLUMN_DATETIME));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(ApplianceHistoryHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(int foreignKey,String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//ApplianceHistoryHelper.COLUMN_STATUS
        sqLiteDatabase.update(ApplianceHistoryHelper.TABLE_NAME_MYPOST, contentValues, ApplianceHistoryHelper.COLUMN_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(ApplianceHistoryHelper.TABLE_NAME_MYPOST, ApplianceHistoryHelper.COLUMN_ID + "=" + id, null);
    }


    public class ApplianceHistoryHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "appliancesHistory_db";
        private static final int DB_VERSION = 4;

        public static final String TABLE_NAME_MYPOST = "appliancesHistory101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_HISTORY = "history";
        public static final String COLUMN_DATETIME = "datetime";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_HISTORY + " TEXT," +
                COLUMN_DATETIME + " TEXT" +
                ");";


        public ApplianceHistoryHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE_MYPOST);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(" DROP TABLE " + TABLE_NAME_MYPOST + " IF EXISTS;");
                onCreate(sqLiteDatabase);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }
}
