package com.homeautomation.homehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.homeautomation.homehub.R;

public class Register extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button = (Button)findViewById(R.id.next);
    }

    public void NextView(View view){
        startActivity(new Intent(Register.this,AddAppliances.class));
    }
}
