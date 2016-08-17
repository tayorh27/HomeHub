package com.homeautomation.homehub.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.homeautomation.homehub.Adapter.HomeHubAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnCheckChangeListener;
import com.homeautomation.homehub.databases.UserLocalDatabase;
import com.homeautomation.homehub.information.Appliance;
import com.homeautomation.homehub.information.History;
import com.homeautomation.homehub.information.User;
import com.homeautomation.homehub.utility.General;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class HomeHub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCheckChangeListener, View.OnClickListener {

    UserLocalDatabase userLocalDatabase;
    User user;
    ImageView uimageView;
    TextView utextView;

    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    TextView lumn;
    String address = null;
    String name = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private OutputStream outputStream;
    private InputStream inputStream;
    //SPP UUID. Look for it

    Toolbar toolbar;
    General general;

    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    RecyclerView recyclerView;
    HomeHubAdapter adapter;
    ArrayList<Appliance> customData = new ArrayList<>();
    Menu myMenu = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(BluetoothConnect.EXTRA_ADDRESS); //receive the address of the bluetooth device
        name = newint.getStringExtra(BluetoothConnect.EXTRA_NAME); //receive the address of the bluetooth device

        setContentView(R.layout.activity_home_hub);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.appliance_recyclerView);
        setSupportActionBar(toolbar);

        uimageView = (ImageView) findViewById(R.id.user_image);
        utextView = (TextView) findViewById(R.id.user_nickname);

        materialDesignFAM = (FloatingActionMenu)findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(this);
        floatingActionButton2.setOnClickListener(this);
        floatingActionButton3.setOnClickListener(this);

        userLocalDatabase = new UserLocalDatabase(HomeHub.this);
        user = userLocalDatabase.getStoredUser();
        general = new General(HomeHub.this);
        adapter = new HomeHubAdapter(HomeHub.this, this);

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.agent_01);//General.getAvatar(user.avatar));
        ///Drawable d = new BitmapDrawable(bitmap);
        //uimageView.setBackground(d);
        //utextView.setText(user.nickname);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setLayout();
        //recyclerView.setLayoutManager(new GridLayoutManager(HomeHub.this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        if (!isBtConnected) {
            new ConnectBT().execute(); //Call the class to connect
        }
        //Toast.makeText(HomeHub.this, "name: " + name + " address: " + address, Toast.LENGTH_SHORT).show();
        DisplayAll();
    }

    private void DisplayAll() {
        try {
            //customData.clear();
            customData = MyApplication.getWritableDatabase().getAllMyPosts();
            adapter.FillAppliance(customData);
        } catch (Exception e) {
            Log.e("Display error", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_hub, menu);
        myMenu = menu;
        return true;
    }

    private void setLayout() {
        String title = userLocalDatabase.getLayout_();
        //MenuItem menuItem = myMenu.findItem(R.id.action_gridList);
        if (title.contentEquals(getResources().getString(R.string.action_grid))) {
            //Toast.makeText(HomeHub.this, "grid", Toast.LENGTH_SHORT).show();
            recyclerView.setLayoutManager(new LinearLayoutManager(HomeHub.this));
            //menuItem.setTitle(getResources().getString(R.string.action_list));
            //menuItem.setIcon(R.drawable.ic_view_quilt_white_24px);
        } else {
            //Toast.makeText(HomeHub.this, "list", Toast.LENGTH_SHORT).show();
            recyclerView.setLayoutManager(new GridLayoutManager(HomeHub.this, 2, LinearLayoutManager.VERTICAL, false));
            //menuItem.setTitle(getResources().getString(R.string.action_grid));
            //menuItem.setIcon(R.drawable.ic_view_list_white_24px);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gridList) {
            MenuItem menuItem = myMenu.findItem(R.id.action_gridList);
            String title = menuItem.getTitle().toString();
            if (title.contentEquals(getResources().getString(R.string.action_grid))) {
                //Toast.makeText(HomeHub.this, "grid", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeHub.this));
                menuItem.setTitle(getResources().getString(R.string.action_list));
                menuItem.setIcon(R.drawable.ic_view_quilt_white_24px);
                userLocalDatabase.setLayoutInput("Grid");
            } else {
                //Toast.makeText(HomeHub.this, "list", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(new GridLayoutManager(HomeHub.this, 2, LinearLayoutManager.VERTICAL, false));
                menuItem.setTitle(getResources().getString(R.string.action_grid));
                menuItem.setIcon(R.drawable.ic_view_list_white_24px);
                userLocalDatabase.setLayoutInput("List");
            }
            DisplayAll();
        }
        if (id == R.id.action_paired) {
            startActivity(new Intent(HomeHub.this, BluetoothConnect.class));
        }
        if (id == R.id.action_get_paired) {
            if (!isBtConnected) {
                new ConnectBT().execute(); //Call the class to connect
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {//HistoryActivity
            startActivity(new Intent(HomeHub.this, CalendarActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(HomeHub.this, ScheduleActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(HomeHub.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void HighPerformance() {
        ArrayList<Appliance> customHigh = MyApplication.getWritableDatabase().getAllMyPosts();
        for (int i = 0; i < customHigh.size(); i++) {
            Appliance current = customHigh.get(i);
            if (current.high) {
                MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "on");
            } else {
                MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "off");
            }
        }
        storeHistory("High Performance mode was switched on.");
        DisplayAll();
        Toast.makeText(HomeHub.this, "High Performance mode has been switched on.", Toast.LENGTH_LONG).show();
        materialDesignFAM.close(true);
    }

    private void Balanced() {
        ArrayList<Appliance> customHigh = MyApplication.getWritableDatabase().getAllMyPosts();
        for (int i = 0; i < customHigh.size(); i++) {
            Appliance current = customHigh.get(i);
            if (current.balanced) {
                MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "on");
            } else {
                MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "off");
            }
        }
        storeHistory("Balanced mode was switched on.");
        DisplayAll();
        Toast.makeText(HomeHub.this, "Balanced mode has been switched on.", Toast.LENGTH_LONG).show();
        materialDesignFAM.close(true);
    }

    private void PowerSaver() {
        ArrayList<Appliance> customHigh = MyApplication.getWritableDatabase().getAllMyPosts();
        for (int i = 0; i < customHigh.size(); i++) {
            Appliance current = customHigh.get(i);
            if (current.saver) {
                MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "on");
            } else {
                MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "off");
            }
        }
        storeHistory("Power Saver mode was switched on.");
        DisplayAll();
        Toast.makeText(HomeHub.this, "Power Saver mode has been switched on.", Toast.LENGTH_LONG).show();
        materialDesignFAM.close(true);
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            } finally {
                //DisplayAll();
            }
        }
        //finish(); //return to the first layout

    }

    private void turnOffLed(String code) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(code.getBytes());
            } catch (IOException e) {
                msg("Error");
            } finally {
                //DisplayAll();
            }
        }
    }

    private void turnOnLed(String code) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(code.getBytes());
            } catch (IOException e) {
                msg("Error");
            } finally {
                //DisplayAll();
            }
        }
    }

    private void ReadData() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    final String string;
                    int byteCount = inputStream.available();
                    if (byteCount > 0) {
                        byte[] rawBytes = new byte[byteCount];
                        inputStream.read(rawBytes);
                        string = new String(rawBytes, "UTF-8");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //return string
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    private void msg(String s) {
        Toast.makeText(HomeHub.this, s, Toast.LENGTH_LONG).show();
    }

    private void storeHistory(String history) {
        ArrayList<History> current = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy hh:mm:ss a");
        String strDate = sdf.format(c.getTime());
        History history1 = new History(history, strDate);
        current.add(history1);
        MyApplication.getWritableHistoryDatabase().insertMyPost(current, false);
    }

    @Override
    public void checkChange(Switch _switch, boolean b, int position) {
        Appliance current = customData.get(position);
        if (!_switch.isChecked()) {
            //Toast.makeText(HomeHub.this, String.valueOf(_switch.isChecked()), Toast.LENGTH_LONG).show();
            //turnOffLed(current.arduinoCode);
            MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "off");//update in turnon method
            storeHistory(current.name + " was switched off.");
            //DisplayAll();
        } else {
            //Toast.makeText(HomeHub.this, String.valueOf(_switch.isChecked()), Toast.LENGTH_LONG).show();
            //turnOnLed(current.arduinoCode);
            MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "on");
            storeHistory(current.name + " was switched on.");
            //DisplayAll();
        }
        DisplayAll();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.material_design_floating_action_menu_item1) {
            HighPerformance();
        }
        if (id == R.id.material_design_floating_action_menu_item2) {
            Balanced();
        }
        if (id == R.id.material_design_floating_action_menu_item3) {
            PowerSaver();
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {//UI THREAD
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(HomeHub.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                //String id = UUID.randomUUID().toString();;;;1b9a4de0-52be-11e6-bdf4-0800200c9a66
                UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//"00001101-0000-1000-8000-00805f9b34fb"
                if (btSocket == null || !isBtConnected) {
                    //Rfcomm d;
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    ParcelUuid[] uuids = dispositivo.getUuids();
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(uuids[0].getUuid());//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                Log.e("Bluetooth - Error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                //finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
                try {
                    outputStream = btSocket.getOutputStream();
                    inputStream = btSocket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                toolbar.setSubtitle("Connected to: " + name);
                //enable relativeLayout
            }
            progress.dismiss();
        }
    }
}
