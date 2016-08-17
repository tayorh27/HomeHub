package com.homeautomation.homehub.utility;

import android.content.Context;

/**
 * Created by Control & Inst. LAB on 17-Aug-16.
 */
public class ConversionToMillis {

    Context context;
    String timeout;
    public ConversionToMillis(Context context, String timeout){
        this.context = context;
        this.timeout = timeout;
    }

    public long returnLong(){
        long millis = 0;
        //SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        if(timeout.contains("Minute(s)")){
            try {
                int getMinute = Integer.parseInt(timeout.substring(0, timeout.indexOf(" ")));
                //Date date = format.parse("00:"+getMinute);
                millis = getMinute*60*1000;//date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(timeout.contains("Hour(s)")){
            int getHour = Integer.parseInt(timeout.substring(0, timeout.indexOf(" ")));
            millis = getHour*60*60*1000;
        }
        return millis;
    }
}
