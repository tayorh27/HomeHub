package com.homeautomation.homehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.databases.UserLocalDatabase;
import com.homeautomation.homehub.information.User;

public class Register extends AppCompatActivity {

    Button button;
    EditText editText;
    RelativeLayout relativeLayout;
    User user;
    UserLocalDatabase userLocalDatabase;
    String getTag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = (Button)findViewById(R.id.next);
        editText = (EditText)findViewById(R.id.nickname);
        relativeLayout = (RelativeLayout)findViewById(R.id.root);

        userLocalDatabase = new UserLocalDatabase(Register.this);
        OnShow();
    }

    public void SelectedImage(View view){
        try {
//            if (getTag.length() > 0) {
//                view.findViewWithTag(getTag).setAlpha((float) 1.0);
//            }
            getTag = view.getTag().toString();
            view.setAlpha((float) 0.2);
            Toast.makeText(Register.this, getTag, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("selectedImage",e.toString());
        }
    }

    public void NextView(View view){
        if(editText.getText().length() > 0) {
            user = new User(getTag,editText.getText().toString());
            userLocalDatabase.StoreUser(user);
            userLocalDatabase.setUserExist(true);
            startActivity(new Intent(Register.this, AddAppliances.class));
        }else{
            Snackbar.make(relativeLayout,"please provide a nickname",Snackbar.LENGTH_LONG).show();
        }
    }

    private void OnShow(){
        User user = userLocalDatabase.getStoredUser();
        String nickname = user.nickname;
        editText.setText(nickname);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(userLocalDatabase.getUserExist()){
            startActivity(new Intent(Register.this, AddAppliances.class));
        }
    }
}
