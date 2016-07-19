package com.homeautomation.homehub.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.homeautomation.homehub.Adapter.AddAppliancesAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnClickListener;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

public class AddAppliances extends AppCompatActivity implements OnClickListener {


    ArrayList<Appliance> current = new ArrayList<>();
    AddAppliancesAdapter adapter;
    RecyclerView recyclerView;
    String getTag = "";
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appliances);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new AddAppliancesAdapter(AddAppliances.this,this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddAppliances.this));
        recyclerView.setAdapter(adapter);
        relativeLayout = (RelativeLayout)findViewById(R.id.root);
        linearLayout = (LinearLayout)findViewById(R.id.noLayout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });
        ShowAll();
    }

    private void ShowAll() {
        ArrayList<Appliance> getAll = MyApplication.getWritableDatabase().getAllMyPosts();
        if(!getAll.isEmpty()){
            linearLayout.setVisibility(View.GONE);
        }
        adapter.FillAppliance(getAll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_appliances_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                //MyApplication.getWritableDatabase().insertMyPost(current,false);
                if(current.size() > 0) {
                    startActivity(new Intent(AddAppliances.this, BluetoothConnect.class));
                }else {
                    Snackbar.make(relativeLayout,"please add at least one appliance",Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SelectColor(View view) {
        try {
//            if (getTag.length() > 0) {
//                view.findViewWithTag(getTag).setAlpha((float) 1.0);
//            }
            getTag = view.getTag().toString();
            view.setAlpha((float) 0.2);
            Toast.makeText(AddAppliances.this, getTag, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("selectedImage",e.toString());
        }
    }

    private void ShowDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(AddAppliances.this).create();
        dialog.setTitle("Add New Appliance");
        dialog.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_add_appliances, (ViewGroup) findViewById(R.id.linearLayout), false);
        final EditText editText = (EditText) view.findViewById(R.id.app_name);
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
                if (editText.getText().length() > 0) {
                    String app_name = editText.getText().toString();
                    //int last = MyApplication.getWritableDatabase().getLastId();
                    Appliance appliance = new Appliance(app_name, getTag);
                    current.add(appliance);
                    MyApplication.getWritableDatabase().insertMyPost(current, false);
                    ShowAll();
                    dialog.dismiss();
                } else {
                    Toast.makeText(AddAppliances.this, "please provide appliance name", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void clicked(View view, int position) {
        ArrayList<Appliance> itemRemove  = MyApplication.getWritableDatabase().getAllMyPosts();
        int id = itemRemove.get(position).id;
        MyApplication.getWritableDatabase().deleteDatabase(id);
        ShowAll();
    }
}
