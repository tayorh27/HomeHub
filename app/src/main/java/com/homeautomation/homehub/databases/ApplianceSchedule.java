package com.homeautomation.homehub.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.homeautomation.homehub.information.Schedule;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 04-Aug-16.
 */
public class ApplianceSchedule {

    ApplianceScheduleHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public ApplianceSchedule(Context context) {
        helper = new ApplianceScheduleHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertMyPost(ArrayList<Schedule> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + ApplianceScheduleHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?,?,?,?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            Schedule current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.app_id);
            statement.bindString(3, current.app_name);
            statement.bindString(4, current.app_code);
            statement.bindString(5, current.app_status);
            statement.bindString(6, current.datetime);
            statement.bindString(7, current.length);
            statement.bindString(8, current.nStatus);
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Schedule> getAllMyPosts() {
        ArrayList<Schedule> currentData = new ArrayList<>();

        String[] columns = {
                ApplianceScheduleHelper.COLUMN_ID,
                ApplianceScheduleHelper.COLUMN_APP_ID,
                ApplianceScheduleHelper.COLUMN_APPNAME,
                ApplianceScheduleHelper.COLUMN_APPCODE,
                ApplianceScheduleHelper.COLUMN_APPSTATUS,
                ApplianceScheduleHelper.COLUMN_DATETIME,
                ApplianceScheduleHelper.COLUMN_LENGTH,
                ApplianceScheduleHelper.COLUMN_NSTATUS
        };
        Cursor cursor = sqLiteDatabase.query(ApplianceScheduleHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Schedule current = new Schedule();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_ID)));
                current.app_id = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_APP_ID));
                current.app_name = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_APPNAME));
                current.app_code = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_APPCODE));
                current.app_status = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_APPSTATUS));
                current.datetime = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_DATETIME));
                current.length = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_LENGTH));
                current.nStatus = cursor.getString(cursor.getColumnIndex(ApplianceScheduleHelper.COLUMN_NSTATUS));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(ApplianceScheduleHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(String foreignKey, String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//ApplianceScheduleHelper.COLUMN_STATUS
        sqLiteDatabase.update(ApplianceScheduleHelper.TABLE_NAME_MYPOST, contentValues, ApplianceScheduleHelper.COLUMN_APP_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(ApplianceScheduleHelper.TABLE_NAME_MYPOST, ApplianceScheduleHelper.COLUMN_ID + "=" + id, null);
    }


    public class ApplianceScheduleHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "appliancesSchedule_db";
        private static final int DB_VERSION = 3;

        public static final String TABLE_NAME_MYPOST = "appliancesSchedule101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_APP_ID = "app_id";
        public static final String COLUMN_APPNAME = "applianceName";
        public static final String COLUMN_APPCODE = "code";
        public static final String COLUMN_APPSTATUS = "mode";
        public static final String COLUMN_DATETIME = "datetime";
        public static final String COLUMN_LENGTH = "length";
        public static final String COLUMN_NSTATUS = "status";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_APP_ID + " TEXT," +
                COLUMN_APPNAME + " TEXT," +
                COLUMN_APPCODE + " TEXT," +
                COLUMN_APPSTATUS + " TEXT," +
                COLUMN_DATETIME + " TEXT," +
                COLUMN_LENGTH + " TEXT," +
                COLUMN_NSTATUS + " TEXT" +
                ");";


        public ApplianceScheduleHelper(Context context) {
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
