package com.homeautomation.homehub.information;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class Appliance {

    public int id;
    public String name,color,arduinoCode,status;
    public boolean high,balanced,saver;

    public Appliance(int id, String name, String color,String arduinoCode){
        this.id = id;
        this.name = name;
        this.color = color;
        this.arduinoCode = arduinoCode;
    }

    public Appliance(String name, String color,String arduinoCode,String status,boolean high,boolean balanced,boolean saver){
        this.name = name;
        this.color = color;
        this.arduinoCode = arduinoCode;
        this.status = status;
        this.high = high;
        this.balanced = balanced;
        this.saver = saver;
    }

//    public Appliance(String name, String color,String arduinoCode,String status,boolean high,boolean balanced,boolean saver){
//        this.name = name;
//        this.color = color;
//        this.arduinoCode = arduinoCode;
//        this.status = status;
//        this.high = high;
//        this.balanced = balanced;
//        this.saver = saver;
//    }

    public Appliance(){

    }
}
