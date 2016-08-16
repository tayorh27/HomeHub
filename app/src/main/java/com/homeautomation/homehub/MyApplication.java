package com.homeautomation.homehub;

import android.app.Application;
import android.content.Context;

import com.homeautomation.homehub.databases.ApplianceHistory;
import com.homeautomation.homehub.databases.ApplianceSchedule;
import com.homeautomation.homehub.databases.AppliancesLocalDB;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    private static AppliancesLocalDB localDB;
    private static ApplianceHistory historyDB;
    private static ApplianceSchedule scheduleDB;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        localDB = new AppliancesLocalDB(this);
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    public synchronized static AppliancesLocalDB getWritableDatabase(){
        if (localDB == null){
            localDB = new AppliancesLocalDB(getAppContext());
        }
        return localDB;

    }

    public synchronized static ApplianceHistory getWritableHistoryDatabase(){
        if (historyDB == null){
            historyDB = new ApplianceHistory(getAppContext());
        }
        return historyDB;

    }

    public synchronized static ApplianceSchedule getWritableScheduleDatabase(){
        if (scheduleDB == null){
            scheduleDB = new ApplianceSchedule(getAppContext());
        }
        return scheduleDB;

    }
}
