package com.homeautomation.homehub.Service;

import android.content.Context;
import android.os.CountDownTimer;

import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.utility.NotificationBar;

/**
 * Created by Control & Inst. LAB on 17-Aug-16.
 */
public class ServiceProcessingTask2 extends CountDownTimer {

    int id;
    String applianceName,arduinoCode,mode,timeout;
    Context context;
    NotificationBar notificationBar;
    public ServiceProcessingTask2(Context context,int id,String applianceName,String arduinoCode,String mode,String timeout, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.id = id;
        this.applianceName = applianceName;
        this.arduinoCode = arduinoCode;
        this.mode = mode;
        this.timeout = timeout;
        notificationBar = new NotificationBar(context);
    }

    private String getStatus(String  mode) {
        if (mode.contentEquals("OFF")) {
            return "on";
        } else {
            return "off";
        }
    }

    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {
        notificationBar.ShowNotification(applianceName+ " has been completed");
        MyApplication.getWritableScheduleDatabase().updateDatabase(String.valueOf(id),"status", "Completed");
        MyApplication.getWritableDatabase().updateDatabase(id,"status", getStatus(mode));
    }
}
