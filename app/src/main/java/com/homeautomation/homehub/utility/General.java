package com.homeautomation.homehub.utility;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.homeautomation.homehub.R;

import java.util.UUID;

/**
 * Created by Control & Inst. LAB on 18-Jul-16.
 */
public class General {

    Context context;
    public General(Context context){
        this.context = context;
    }
    public static int getAvatar(String image) {
        int avatar = 0;
        switch (image){
            case "agent_01":
                avatar = R.drawable.agent_01;
                break;
            case "agent_01_01":
                avatar = R.drawable.agent_01_01;
                break;
            case "anonymous2_girl_material":
                avatar = R.drawable.anonymous2_girl_material;
                break;
            case "anonymous_material_01":
                avatar = R.drawable.anonymous_material_01;
                break;
            case "bride_hispanic_material_01":
                avatar = R.drawable.bride_hispanic_material_01;
                break;
            case "cook_woman_fast_food_redhead_material_01":
                avatar = R.drawable.cook_woman_fast_food_redhead_material_01;
                break;
            case "cool_man_material_01":
                avatar = R.drawable.cool_man_material_01;
                break;
            case "engineering2_material_01":
                avatar = R.drawable.engineering2_material_01;
                break;
            case "engineering_woman_blonde_material_01":
                avatar = R.drawable.engineering_woman_blonde_material_01;
                break;
            case "football_soccer_man_material_01":
                avatar = R.drawable.football_soccer_man_material_01;
                break;
            case "man_material_01":
                avatar = R.drawable.man_material_01;
                break;
            case "browsed_image":
                avatar = R.drawable.agent_01;
                break;
            default:
                avatar = R.drawable.anonymous3_material_01;
                break;
        }
        return avatar;
    }

    public static int getColor(String tag) {
        int color;
        if(tag.contentEquals("color1")){
            color = R.color.red;
        }else if(tag.contentEquals("color2")){
            color = R.color.pink;
        }else if(tag.contentEquals("color3")){
            color = R.color.purple;
        }else if(tag.contentEquals("color4")){
            color = R.color.deep_purple;
        }else if(tag.contentEquals("color5")){
            color = R.color.indigo;
        }else if(tag.contentEquals("color6")){
            color = R.color.blue;
        }else if(tag.contentEquals("color7")){
            color = R.color.light_blue;
        }else if(tag.contentEquals("color8")){
            color = R.color.cyan;
        }else if(tag.contentEquals("color9")){
            color = R.color.teal;
        }else if(tag.contentEquals("color10")){
            color = R.color.green;
        }else if(tag.contentEquals("color11")){
            color = R.color.light_green;
        }else if(tag.contentEquals("color12")){
            color = R.color.lime;
        }else if(tag.contentEquals("color13")){
            color = R.color.yellow;
        }else if(tag.contentEquals("color14")){
            color = R.color.amber;
        }else if(tag.contentEquals("color15")){
            color = R.color.orange;
        }else if(tag.contentEquals("color16")){
            color = R.color.deep_orange;
        }else if(tag.contentEquals("color17")){
            color = R.color.brown;
        }else if(tag.contentEquals("color18")){
            color = R.color.grey;
        }else if(tag.contentEquals("color19")){
            color = R.color.blue_grey;
        }else if(tag.contentEquals("color20")){
            color = R.color.black;
        }else {
            color = R.color.colorPrimary;
        }
//        switch (tag) {
//            case "color1":
//                color = R.color.red;
//                break;
//            case "color2":
//                color = R.color.pink;
//                break;
//            case "color3":
//                color = R.color.purple;
//                break;
//            case "color4":
//                color = R.color.deep_purple;
//                break;
//            case "color5":
//                color = R.color.indigo;
//                break;
//            case "color6":
//                color = R.color.blue;
//                break;
//            case "color7":
//                color = R.color.light_blue;
//                break;
//            case "color8":
//                color = R.color.cyan;
//                break;
//            case "color9":
//                color = R.color.teal;
//                break;
//            case "color10":
//                color = R.color.green;
//                break;
//            case "color11":
//                color = R.color.light_green;
//                break;
//            case "color12":
//                color = R.color.lime;
//                break;
//            case "color13":
//                color = R.color.yellow;
//                break;
//            case "color14":
//                color = R.color.amber;
//                break;
//            case "color15":
//                color = R.color.orange;
//                break;
//            case "color16":
//                color = R.color.deep_orange;
//                break;
//            case "color17":
//                color = R.color.brown;
//                break;
//            case "color18":
//                color = R.color.grey;
//                break;
//            case "color19":
//                color = R.color.blue_grey;
//                break;
//            case "color20":
//                color = R.color.black;
//                break;
//            default:
//                color = R.color.black;
//                break;
//        }
        return color;
    }

    public String getDeviceID(){
//        String device="";
//        device = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        return device;
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmSerial = teleManager.getSimSerialNumber();
        String tmDeviceId = teleManager.getDeviceId();
        String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        if (tmSerial  == null) tmSerial   = "1";
        if (tmDeviceId== null) tmDeviceId = "1";
        if (androidId == null) androidId  = "1";
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDeviceId.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
}
