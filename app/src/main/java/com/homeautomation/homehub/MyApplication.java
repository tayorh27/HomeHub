package com.homeautomation.homehub;

import android.app.Application;
import android.content.Context;

import com.homeautomation.homehub.databases.AppliancesLocalDB;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;
    private static AppliancesLocalDB localDB;

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
}
