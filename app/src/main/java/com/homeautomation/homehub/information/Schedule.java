package com.homeautomation.homehub.information;

/**
 * Created by Control & Inst. LAB on 04-Aug-16.
 */
public class Schedule {

    public int id;
    public String app_name,app_code,app_status,datetime,length,nStatus;

    public Schedule(){

    }

    public Schedule(String app_name,String app_code,String app_status,String datetime,String length, String nStatus){
        this.app_name = app_name;
        this.app_code = app_code;
        this.app_status = app_status;
        this.datetime = datetime;
        this.length = length;
        this.nStatus = nStatus;//pending,in-progress,completed
    }
}
