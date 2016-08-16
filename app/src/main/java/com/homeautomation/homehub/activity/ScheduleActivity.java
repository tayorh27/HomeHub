package com.homeautomation.homehub.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.homeautomation.homehub.Adapter.ScheduleAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnClickListener;
import com.homeautomation.homehub.information.Appliance;
import com.homeautomation.homehub.information.History;
import com.homeautomation.homehub.information.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity implements OnClickListener, View.OnClickListener {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    ArrayList<Schedule> getAll = new ArrayList<>();
    ScheduleAdapter adapter;
    private int hour, min;
    private String setTime_ = "";
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        showTime(hour,min, String.valueOf(Calendar.AM_PM));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_schedule);
        relativeLayout = (RelativeLayout)findViewById(R.id.schedule_home);
        linearLayout = (LinearLayout)findViewById(R.id.noLayout);
        adapter = new ScheduleAdapter(ScheduleActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScheduleActivity.this));
        recyclerView.setAdapter(adapter);
        ShowAll();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayDialog();
            }
        });
    }

    private void showTime(int hour, int min, String am_pm) {
        String _setTime = String.valueOf(new StringBuilder().append(hour).append(":")
                .append(min).append(" "+am_pm));
        setTime_ = _setTime;
    }

    private TimePickerDialog.OnTimeSetListener mTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            ViewGroup vg=(ViewGroup) timePicker.getChildAt(0);
            String am_pm = ((Button)vg.getChildAt(2)).getText().toString();
            showTime(i,i1,am_pm);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 999){
            return new TimePickerDialog(this, mTime, hour, min, false);
        }
        return super.onCreateDialog(id);
    }

    private ArrayList<String> get_all_appliance_name(){
        ArrayList<Appliance> gApp = MyApplication.getWritableDatabase().getAllMyPosts();
        ArrayList<String> retApp = new ArrayList<>();
        for (Appliance item:gApp) {
            retApp.add(item.name);//+"/"+item.arduinoCode
        }
        return  retApp;
    }

    private String getStatus(boolean mode){
        if(mode){
            return "ON";
        }else {
            return "OFF";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_clear_all){
            ConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ConfirmationDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ScheduleActivity.this).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("Scheduled Appliances");
        alertDialog.setMessage("Are you sure you want to clear all scheduled appliances?");
        alertDialog.setButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                ShowAll1();
            }
        });
        alertDialog.setButton2("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyApplication.getWritableScheduleDatabase().deleteAll();
                ShowAll1();
                alertDialog.dismiss();
                Toast.makeText(ScheduleActivity.this,"Scheduled Appliances have been cleared.",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void DisplayDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(ScheduleActivity.this).create();
        dialog.setTitle("Add New Schedule");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_add_schedule_layout, (ViewGroup) findViewById(R.id.custom_layout), false);
        final Spinner app_spin = (Spinner)view.findViewById(R.id.appliances_spinner);
        final Switch app_mode = (Switch) view.findViewById(R.id.app_switch);
        final Spinner timeout_spin = (Spinner)view.findViewById(R.id.timeout_spinner);
        final EditText editText = (EditText) view.findViewById(R.id.et_timeout);
        final Button setTime = (Button)view.findViewById(R.id.btn_time);
        final TextView tvMode = (TextView)view.findViewById(R.id.tv_mode);
        app_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(app_mode.isChecked()) {
                    tvMode.setText("Set Appliance mode: ON");
                }else {
                    tvMode.setText("Set Appliance mode: OFF");
                }
            }
        });
        setTime.setOnClickListener(this);
        setTime.setText(setTime_);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(ScheduleActivity.this,android.R.layout.simple_list_item_1,get_all_appliance_name());
        app_spin.setAdapter(adapt);
        dialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                ShowAll();
            }
        });
        dialog.setButton2("Schedule Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText().length() > 0) {
                    String app_name = app_spin.getSelectedItem().toString();
                    int app_position = app_spin.getSelectedItemPosition();
                    String app_code = "arduino"+(app_position+1);
                    String app_status = getStatus(app_mode.isChecked());
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");//hh:mm:ss a
                    String datetime = sdf.format(c.getTime())+ " "+setTime_;
                    String length = editText.getText().toString()+" "+timeout_spin.getSelectedItem().toString();
                    ArrayList<Schedule> current = new ArrayList<>();
                    Schedule sc = new Schedule(app_name,app_code,app_status,datetime,length,"Pending");
                    current.add(sc);
                    MyApplication.getWritableScheduleDatabase().insertMyPost(current, false);
                    ArrayList<History> storeHis = new ArrayList<History>();
                    History his = new History(app_name+" was scheduled to "+app_status+" for "+length,datetime);
                    storeHis.add(his);
                    MyApplication.getWritableHistoryDatabase().insertMyPost(storeHis,false);
                    ShowAll();
                    dialog.dismiss();
                } else {
                    Toast.makeText(ScheduleActivity.this, "please enter a timeout session", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void ShowAll() {
        getAll = MyApplication.getWritableScheduleDatabase().getAllMyPosts();
        if(!getAll.isEmpty()){
            linearLayout.setVisibility(View.GONE);
        }
        adapter.FillSchedule(getAll);
    }

    private void ShowAll1() {
        ArrayList<Schedule> getAll1 = MyApplication.getWritableScheduleDatabase().getAllMyPosts();
        if(!getAll1.isEmpty()){
            linearLayout.setVisibility(View.GONE);
        }
        adapter.FillSchedule(getAll1);
    }

    @Override
    public void clicked(View view, int position) {
        int id = getAll.get(position).id;
        final String n = getAll.get(position).app_name;
        final String cd = getAll.get(position).app_code;
        final String s = getAll.get(position).app_status;
        final String d = getAll.get(position).datetime;
        final String l = getAll.get(position).length;
        final String ns = getAll.get(position).nStatus;
        MyApplication.getWritableScheduleDatabase().deleteDatabase(id);
        ShowAll();
        Snackbar.make(relativeLayout,"History deleted.",Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Schedule> c = new ArrayList<>();
                Schedule sch = new Schedule(n,cd,s,d,l,ns);
                c.add(sch);
                MyApplication.getWritableScheduleDatabase().insertMyPost(c,false);
                ShowAll();
            }
        }).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_time){
            ShowDialog();
        }
    }

    private void ShowDialog(){
        showDialog(999);
    }
}
