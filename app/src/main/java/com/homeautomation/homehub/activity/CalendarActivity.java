package com.homeautomation.homehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.homeautomation.homehub.R;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                String[] dates = {"January","February","March","April","May","June","July","August","September","October","November","December"};
                String dt = i2 + "/" + dates[i1] + "/" + i;
                //Toast.makeText(CalendarActivity.this,dt,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CalendarActivity.this, HistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("date", dt);
                bundle.putString("option","forDate");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view_all) {
            Intent intent = new Intent(CalendarActivity.this, HistoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("date", "dt");
            bundle.putString("option","forView");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
