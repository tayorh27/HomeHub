package com.homeautomation.homehub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.homeautomation.homehub.activity.Register;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.main_imageView);

        ArrayList<Appliance> c = new ArrayList<>();
        Appliance appliance = new Appliance("none", "", "","");
        c.add(appliance);
        MyApplication.getWritableDatabase().insertMyPost(c, false);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
