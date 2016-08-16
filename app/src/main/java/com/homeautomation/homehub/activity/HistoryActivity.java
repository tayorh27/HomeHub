package com.homeautomation.homehub.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.homeautomation.homehub.Adapter.HistoryAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnClickListener;
import com.homeautomation.homehub.information.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements OnClickListener {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    ArrayList<History> getAll = new ArrayList<>();
    HistoryAdapter adapter;
    String dt;
    String opt;
    ArrayList<History> current = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_history);
        relativeLayout = (RelativeLayout) findViewById(R.id.history_home);
        linearLayout = (LinearLayout) findViewById(R.id.noLayout);
        adapter = new HistoryAdapter(HistoryActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        recyclerView.setAdapter(adapter);
        DisplayHistory();
        //ShowAll();
    }

    private void DisplayHistory() {
        try {
            getAll = MyApplication.getWritableHistoryDatabase().getAllMyPosts();
            if (!getAll.isEmpty()) {
                linearLayout.setVisibility(View.GONE);
            }
            Bundle bundle = getIntent().getExtras();
            dt = bundle.getString("date");
            opt = bundle.getString("option");
            if (opt.contentEquals("forDate")) {
                for (int i = 0; i < getAll.size(); i++) {
                    if (getAll.get(i).dateTime.contains(dt)) {
                        current.add(getAll.get(i));
                    }
                }
                if (!current.isEmpty()) {
                    linearLayout.setVisibility(View.GONE);
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                adapter.FillAppliance(current);
            } else {
                adapter.FillAppliance(getAll);
            }
        } catch (Exception e) {
            Log.e("error from History", e.toString());
        }
    }

    private void ShowAll() {
        getAll = MyApplication.getWritableHistoryDatabase().getAllMyPosts();
        if (!getAll.isEmpty()) {
            linearLayout.setVisibility(View.GONE);
        }
        adapter.FillAppliance(getAll);
    }

    private void ShowAll1() {
        ArrayList<History> getAll1 = MyApplication.getWritableHistoryDatabase().getAllMyPosts();
        if (!getAll1.isEmpty()) {
            linearLayout.setVisibility(View.GONE);
        }
        adapter.FillAppliance(getAll1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_all) {
            ConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ConfirmationDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("History");
        alertDialog.setMessage("Are you sure you want to clear all history?");
        alertDialog.setButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                DisplayHistory();
            }
        });
        alertDialog.setButton2("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opt.contentEquals("forDate")){
                    for (int a = 0; a < current.size(); a++) {
                        MyApplication.getWritableHistoryDatabase().deleteDatabase(current.get(a).id);
                    }
                }else {
                    MyApplication.getWritableHistoryDatabase().deleteAll();
                }
                DisplayHistory();
                Toast.makeText(HistoryActivity.this, "History has been cleared.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HistoryActivity.this, CalendarActivity.class));
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void clicked(View view, int position) {
        int id = getAll.get(position).id;
        final String his = getAll.get(position).history;
        final String dt = getAll.get(position).dateTime;
        MyApplication.getWritableHistoryDatabase().deleteDatabase(id);
        DisplayHistory();
        Snackbar.make(relativeLayout, "History deleted.", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<History> c = new ArrayList<>();
                History hist = new History(his, dt);
                c.add(hist);
                MyApplication.getWritableHistoryDatabase().insertMyPost(c, false);
                DisplayHistory();
            }
        }).show();
    }
}
