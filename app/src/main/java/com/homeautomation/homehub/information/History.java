package com.homeautomation.homehub.information;

/**
 * Created by Control & Inst. LAB on 04-Aug-16.
 */
public class History {

    public int id;
    public String history,dateTime;

    public History(int id, String history, String dateTime){
        this.id = id;
        this.history = history;
        this.dateTime = dateTime;
    }

    public History(String history, String dateTime){
        this.history = history;
        this.dateTime = dateTime;
    }

    public History(){

    }
}
