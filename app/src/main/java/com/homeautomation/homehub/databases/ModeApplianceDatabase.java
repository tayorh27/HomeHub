package com.homeautomation.homehub.databases;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Control & Inst. LAB on 02-Aug-16.
 */
public class ModeApplianceDatabase {

    Context context;
    private SharedPreferences userPreference;
    private String userDetails = "saveModes";

    public ModeApplianceDatabase(Context context){
        this.context = context;
        userPreference = context.getSharedPreferences(userDetails,0);
    }

    public void storeHigh(String high){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("high",high);
        editor.apply();
    }

    public String getHigh(){
        return userPreference.getString("high","");
    }

    public void storeBalanced(String balanced){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("balanced",balanced);
        editor.apply();
    }

    public String getBalanced(){
        return userPreference.getString("balanced","");
    }

    public void storeSaver(String saver){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("saver",saver);
        editor.apply();
    }

    public String getSaver(){
        return userPreference.getString("saver","");
    }

    public void clearAll(){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.clear();
        editor.apply();
    }
}
