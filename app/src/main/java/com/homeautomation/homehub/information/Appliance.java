package com.homeautomation.homehub.information;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class Appliance {

    public int id;
    public String name,color;

    public Appliance(int id, String name, String color){
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Appliance(String name, String color){
        this.name = name;
        this.color = color;
    }

    public Appliance(){

    }
}
