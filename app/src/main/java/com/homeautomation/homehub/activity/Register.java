package com.homeautomation.homehub.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.databases.UserLocalDatabase;
import com.homeautomation.homehub.information.Appliance;
import com.homeautomation.homehub.information.User;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    Button button;
    EditText editText;
    RelativeLayout relativeLayout;
    User user;
    UserLocalDatabase userLocalDatabase;
    String getTag = "";
    private final static int LOAD_IMAGES_PICK = 1;
    String picturePath = "";
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = (Button) findViewById(R.id.next);
        editText = (EditText) findViewById(R.id.nickname);
        relativeLayout = (RelativeLayout) findViewById(R.id.root);
        iv = (ImageView)findViewById(R.id.avatar12);

        userLocalDatabase = new UserLocalDatabase(Register.this);
        ArrayList<Appliance> c = new ArrayList<>();
        Appliance appliance = new Appliance("none", "", "", "", false, false, false);
        c.add(appliance);
        //MyApplication.getWritableDatabase().insertMyPost(c, false);
        _OnShow();
    }

    public void SelectedImage(View view) {
        try {
//            if (getTag.length() > 0) {
//                view.findViewWithTag(getTag).setAlpha((float) 1.0);
//            }
            int id = view.getId();
            if (id == R.id.avatar12) {
                view.setAlpha((float) 0.2);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGES_PICK);
            } else {
                getTag = view.getTag().toString();
                view.setAlpha((float) 0.2);
                //Toast.makeText(Register.this, getTag, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("selectedImage", e.toString());
        }
    }

    public void NextView(View view) {
        if (editText.getText().length() > 0) {
            user = new User(getTag, editText.getText().toString());
            userLocalDatabase.StoreUser(user);
            userLocalDatabase.setUserExist(true);
            startActivity(new Intent(Register.this, AddAppliances.class));
        } else {
            Snackbar.make(relativeLayout, "please provide a nickname", Snackbar.LENGTH_LONG).show();
        }
    }

    private void _OnShow() {
        User user = userLocalDatabase.getStoredUser();
        String nickname = user.nickname;
        editText.setText(nickname);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGES_PICK && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            getTag = "browsed_image/"+picturePath;

            cursor.close();
            iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userLocalDatabase.getUserExist()) {
            startActivity(new Intent(Register.this, AddAppliances.class));
            finish();
        }
    }
}
