package com.homeautomation.homehub.Service;

import android.content.Context;
import android.os.CountDownTimer;

import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.utility.ConversionToMillis;
import com.homeautomation.homehub.utility.NotificationBar;
import com.homeautomation.homehub.utility.TimeUtils;

/**
 * Created by Control & Inst. LAB on 15-Aug-16.
 */
public class ServiceProcessingTask extends CountDownTimer {

    int id,schedule_id;
    String applianceName, arduinoCode, mode, timeout;
    Context context;
    NotificationBar notificationBar;

    public ServiceProcessingTask(Context context, int id, int schedule_id,String applianceName, String arduinoCode, String mode, String timeout, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.id = id;
        this.schedule_id = schedule_id;
        this.applianceName = applianceName;
        this.arduinoCode = arduinoCode;
        this.mode = mode;
        this.timeout = timeout;
        notificationBar = new NotificationBar(context);
    }

    @Override
    public void onTick(long l) {
        MyApplication.getWritableScheduleDatabase().updateDatabase(schedule_id, "status", TimeUtils.TimeLeft(l));
    }

    @Override
    public void onFinish() {
        notificationBar.ShowNotification(applianceName + "\n has started to schedule to " + mode + "\n for " + timeout);
        MyApplication.getWritableScheduleDatabase().updateDatabase(schedule_id, "status", "In-progress");
        MyApplication.getWritableDatabase().updateDatabase(id,"status", mode.toLowerCase());
        ConversionToMillis conversionToMillis = new ConversionToMillis(context, timeout);
        ServiceProcessingTask2 serviceProcessingTask2 = new ServiceProcessingTask2(context, id,schedule_id, applianceName, arduinoCode, mode, timeout, conversionToMillis.returnLong(), 1000);
        serviceProcessingTask2.start();
    }
}
