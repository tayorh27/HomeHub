package com.homeautomation.homehub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.homeautomation.homehub.activity.Register;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Appliance> c = new ArrayList<>();
        Appliance appliance = new Appliance(1,"none","");
        c.add(appliance);
        //MyApplication.getWritableDatabase().insertMyPost(c,false);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
