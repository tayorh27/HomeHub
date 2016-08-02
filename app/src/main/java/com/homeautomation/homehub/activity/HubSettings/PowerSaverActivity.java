package com.homeautomation.homehub.activity.HubSettings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.homeautomation.homehub.Adapter.HubSettings.HubSettingsAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.SettingsCheckListener;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

public class PowerSaverActivity extends AppCompatActivity implements SettingsCheckListener {

    RecyclerView recyclerView;
    HubSettingsAdapter adapter;
    ArrayList<Appliance> customData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_saver);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_power);
        adapter = new HubSettingsAdapter(PowerSaverActivity.this,this,"saver");
        recyclerView.setLayoutManager(new LinearLayoutManager(PowerSaverActivity.this));
        recyclerView.setAdapter(adapter);
        customData = MyApplication.getWritableDatabase().getAllMyPosts();
        adapter.FillRecyclerView(customData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu_settings_all, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_info){
            displayDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(PowerSaverActivity.this).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("Info");
        alertDialog.setMessage("Select which appliances you want to TURN ON if this mode is to be selected.\nThanks for choosing HomeHub");
        alertDialog.show();
    }

    @Override
    public void onSettingsCheckListener(CompoundButton compoundButton, boolean checked, int position) {

    }

    @Override
    public void onSettingsRelativeCheckListener(CheckBox checkBox, boolean checked, int position) {

    }
}
