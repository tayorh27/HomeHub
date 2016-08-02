package com.homeautomation.homehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.activity.HubSettings.BalancedActivity;
import com.homeautomation.homehub.activity.HubSettings.HighPerformanceActivity;
import com.homeautomation.homehub.activity.HubSettings.PowerSaverActivity;

public class SettingsActivity extends AppCompatActivity {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lv = (ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(SettingsActivity.this, HighPerformanceActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(SettingsActivity.this, BalancedActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(SettingsActivity.this, PowerSaverActivity.class));
                        break;
                }
            }
        });
    }
}
