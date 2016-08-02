package com.homeautomation.homehub.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class AppliancesLocalDB {

    ApplianceHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public AppliancesLocalDB(Context context) {
        helper = new ApplianceHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertMyPost(ArrayList<Appliance> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + ApplianceHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?,?,?,?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            Appliance current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.name);
            statement.bindString(3, current.color);
            statement.bindString(4, current.arduinoCode);
            statement.bindString(5, current.status);
            statement.bindString(6, String.valueOf(current.high));
            statement.bindString(7, String.valueOf(current.balanced));
            statement.bindString(8, String.valueOf(current.saver));
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Appliance> getAllMyPosts() {
        ArrayList<Appliance> currentData = new ArrayList<>();

        String[] columns = {
                ApplianceHelper.COLUMN_ID,
                ApplianceHelper.COLUMN_APP_NAME,
                ApplianceHelper.COLUMN_COLOR,
                ApplianceHelper.COLUMN_ARDUIONOCODE,
                ApplianceHelper.COLUMN_STATUS,
                ApplianceHelper.COLUMN_HIGH,
                ApplianceHelper.COLUMN_BALANCED,
                ApplianceHelper.COLUMN_SAVER
        };
        Cursor cursor = sqLiteDatabase.query(ApplianceHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Appliance current = new Appliance();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_ID)));
                current.name = cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_APP_NAME));
                current.color = cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_COLOR));
                current.arduinoCode = cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_ARDUIONOCODE));
                current.status = cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_STATUS));
                current.high = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_HIGH)));
                current.balanced = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_BALANCED)));
                current.saver = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ApplianceHelper.COLUMN_SAVER)));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public int getLastId() {
        int id = 1;
        String[] columns = {
                ApplianceHelper.COLUMN_ID,
                ApplianceHelper.COLUMN_APP_NAME,
                ApplianceHelper.COLUMN_COLOR,
                ApplianceHelper.COLUMN_ARDUIONOCODE,
                ApplianceHelper.COLUMN_STATUS,
                ApplianceHelper.COLUMN_HIGH,
                ApplianceHelper.COLUMN_BALANCED,
                ApplianceHelper.COLUMN_SAVER
        };
        Cursor cursor = sqLiteDatabase.query(ApplianceHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            id = cursor.getInt(0);
            //cursor.close();
        }
        Log.e("gotID", "my id is " + id);
        return id;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(ApplianceHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(int foreignKey,String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//ApplianceHelper.COLUMN_STATUS
        sqLiteDatabase.update(ApplianceHelper.TABLE_NAME_MYPOST, contentValues, ApplianceHelper.COLUMN_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(ApplianceHelper.TABLE_NAME_MYPOST, ApplianceHelper.COLUMN_ID + "=" + id, null);
    }


    public class ApplianceHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "appliances_db";
        private static final int DB_VERSION = 3;

        public static final String TABLE_NAME_MYPOST = "appliances101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_APP_NAME = "applianceName";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_ARDUIONOCODE = "arduino_code";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_HIGH = "high";
        public static final String COLUMN_BALANCED = "balanced";
        public static final String COLUMN_SAVER = "saver";
//        public static final String COLUMN_IMAGE = "image";
//        public static final String COLUMN_MOBILE = "mobile";
//        public static final String COLUMN_USERNAME = "username";
//        public static final String COLUMN_STATUS = "status";
//        public static final String COLUMN_TIME = "created_time";
//        public static final String COLUMN_DISPLAY = "display";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_APP_NAME + " TEXT," +
                COLUMN_COLOR + " TEXT," +
                COLUMN_ARDUIONOCODE + " TEXT," +
                COLUMN_STATUS + " TEXT," +
                COLUMN_HIGH + " TEXT," +
                COLUMN_BALANCED + " TEXT," +
                COLUMN_SAVER + " TEXT" +
                ");";


        public ApplianceHelper(Context context) {
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
