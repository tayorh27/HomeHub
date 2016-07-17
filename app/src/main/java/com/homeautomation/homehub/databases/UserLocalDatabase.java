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

    public void setUserExist(boolean log){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putBoolean("logged",log);
        editor.apply();
    }

    public boolean getUserExist(){
        return userPreference.getBoolean("logged",false);
    }

    public void clearUser(){
        SharedPreferences.Editor editor = userPreference.edit();
        editor.clear();
        editor.apply();
    }
}
