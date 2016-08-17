package com.homeautomation.homehub.utility;

import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 17-Aug-16.
 */
public class GetAppDetails {

    public static String[] details(String name){
        String[] gDetails = new String[2];
        ArrayList<Appliance> getAppliances = MyApplication.getWritableDatabase().getAllMyPosts();
        for (int i = 0; i < getAppliances.size(); i++){
            if(getAppliances.get(i).name.contentEquals(name)){
                gDetails[0] = getAppliances.get(i).id+"";
                gDetails[1] = getAppliances.get(i).arduinoCode;
            }
        }
        return gDetails;
    }

}
