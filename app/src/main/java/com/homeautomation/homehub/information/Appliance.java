package com.homeautomation.homehub.information;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class Appliance {

    public int id;
    public String name,color,arduinoCode,status;

    public Appliance(int id, String name, String color,String arduinoCode){
        this.id = id;
        this.name = name;
        this.color = color;
        this.arduinoCode = arduinoCode;
    }

    public Appliance(String name, String color,String arduinoCode,String status){
        this.name = name;
        this.color = color;
        this.arduinoCode = arduinoCode;
        this.status = status;
    }

    public Appliance(){

    }
}
