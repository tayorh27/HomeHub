package com.homeautomation.homehub.utility;

/**
 * Created by Control & Inst. LAB on 17-Aug-16.
 */
public class TimeUtils {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;


    public static String TimeLeft(long milli){
        String outTime = "";

        double days = Math.floor((double) milli/ONE_DAY);
        double hours = Math.floor((double) (milli % ONE_DAY) / ONE_HOUR);
        double minutes = Math.floor((double) (milli % ONE_HOUR) / ONE_MINUTE);
        double seconds = Math.floor((double) (milli % ONE_MINUTE) / ONE_SECOND);

        int d = (int)days;
        int h = (int)hours;
        int m = (int)minutes;
        int s = (int)seconds;

        if(days > 0){
            if(d == 1){
                outTime = String.valueOf(d)+"day left";
            }else {
                outTime = String.valueOf(d) + "days left";
            }
        }else if(hours > 0){
            if(h == 1) {
                outTime = String.valueOf(h) + "hr left";
            }else {
                outTime = String.valueOf(h) + "hrs left";
            }
        }else if(minutes > 0){
            if(m == 1) {
                outTime = String.valueOf(m) + "min left";
            }else {
                outTime = String.valueOf(m) + "mins left";
            }
        }else if(seconds > 0){
            outTime = String.valueOf(s)+"secs left";
        }else {
            outTime = "moments left";
        }

        return outTime;

    }
}
