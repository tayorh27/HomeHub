package com.homeautomation.homehub.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.homeautomation.homehub.Adapter.AddAppliancesAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

public class AddAppliances extends AppCompatActivity {


    ArrayList<Appliance> current = new ArrayList<>();
    AddAppliancesAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appliances);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new AddAppliancesAdapter(AddAppliances.this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddAppliances.this));
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });
    }

    private void ShowAll(){
        adapter.FillAppliance(MyApplication.getWritableDatabase().getAllMyPosts());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_appliances_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:
                //MyApplication.getWritableDatabase().insertMyPost(current,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(AddAppliances.this).create();
        dialog.setTitle("Add New Appliance");
        dialog.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_add_appliances,(ViewGroup)findViewById(R.id.linearLayout),false);
        final EditText editText = (EditText)view.findViewById(R.id.app_name);
        dialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                ShowAll();
            }
        });
        dialog.setButton2("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(editText.getText().length() > 0) {
                    String app_name = editText.getText().toString();
                    //int last = MyApplication.getWritableDatabase().getLastId();
                    Appliance appliance = new Appliance(app_name, "");
                    current.add(appliance);
                    MyApplication.getWritableDatabase().insertMyPost(current,false);
                    ShowAll();
                    dialog.dismiss();
                }else {
                    Toast.makeText(AddAppliances.this, "please provide appliance name",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

}
