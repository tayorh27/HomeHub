package com.homeautomation.homehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.databases.UserLocalDatabase;
import com.homeautomation.homehub.information.User;

public class Register extends AppCompatActivity {

    Button button;
    EditText editText;
    RelativeLayout relativeLayout;
    User user;
    UserLocalDatabase userLocalDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = (Button)findViewById(R.id.next);
        editText = (EditText)findViewById(R.id.nickname);
        relativeLayout = (RelativeLayout)findViewById(R.id.root);

        userLocalDatabase = new UserLocalDatabase(Register.this);
    }

    public void SelectedImage(View view){
        switch (view.getId()){
            case R.id.avatar1:

                break;
        }
    }

    public void NextView(View view){
        if(editText.getText().length() > 0) {
            user = new User("",editText.getText().toString());
            userLocalDatabase.StoreUser(user);
            userLocalDatabase.setUserExist(true);
            startActivity(new Intent(Register.this, AddAppliances.class));
        }else{
            Snackbar.make(relativeLayout,"please provide a nickname",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(userLocalDatabase.getUserExist()){
            startActivity(new Intent(Register.this, AddAppliances.class));
        }
    }
}
