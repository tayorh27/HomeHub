package com.homeautomation.homehub.databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.homeautomation.homehub.information.User;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class UserLocalDatabase {
    Context context;
    private SharedPreferences userPreference;
    private String userDetails = "saveDetails";

    public UserLocalDatabase(Context context){
        this.context = context;
        userPreference = context.getSharedPreferences(userDetails,0);
    }

    public void StoreUser(User user){
        String avatar = user.avatar;
        String nickname = user.nickname;
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("avatar",avatar);
        editor.putString("nickname",nickname);
        editor.apply();
    }

    public User getStoredUser(){
        String avatar = userPreference.getString("avatar","");
        String nickname = userPreference.getString("nickname","");
        User user = new User(avatar,nickname);
        return user;
    }

    public void setAppliancesAdded(boolean add){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putBoolean("add",add);
        editor.apply();
    }

    public boolean getAppliancesAdded(){
        return userPreference.getBoolean("add",false);
    }

    public void setUserExist(boolean add){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putBoolean("logged",add);
        editor.apply();
    }

    public boolean getUserExist(){
        return userPreference.getBoolean("logged",false);
    }

    public void setCounterInput(int counter){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putInt("counter", counter);
        editor.apply();
    }

    public int getCounter(){
        return userPreference.getInt("counter", 1);
    }

    public void setLayoutInput(String layout){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString("layout", layout);
        editor.apply();
    }

    public String getLayout_(){
        return userPreference.getString("layout", "grid");
    }

    public void clearUser(){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.clear();
        editor.apply();
    }
}
