package com.homeautomation.homehub.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.homeautomation.homehub.Adapter.HomeHubAdapter;
import com.homeautomation.homehub.MyApplication;
import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnCheckChangeListener;
import com.homeautomation.homehub.databases.UserLocalDatabase;
import com.homeautomation.homehub.information.Appliance;
import com.homeautomation.homehub.information.User;
import com.homeautomation.homehub.utility.General;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class HomeHub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCheckChangeListener {

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
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    RecyclerView recyclerView;
    HomeHubAdapter adapter;
    ArrayList<Appliance> customData = new ArrayList<>();


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

        recyclerView.setLayoutManager(new GridLayoutManager(HomeHub.this, 2, LinearLayoutManager.VERTICAL, false));
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(HomeHub.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void ReadData(){
        Thread thread = new Thread(){
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
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    private void msg(String s) {
        Toast.makeText(HomeHub.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void checkChange(CompoundButton compoundButton, boolean b, int position) {
        ArrayList<Appliance> getA = MyApplication.getWritableDatabase().getAllMyPosts();
        Appliance current = getA.get(position);
        if (b) {
            Toast.makeText(HomeHub.this, String.valueOf(b), Toast.LENGTH_LONG).show();
            //turnOffLed(current.arduinoCode);
            MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "off");
            //DisplayAll();
        } else {
            Toast.makeText(HomeHub.this, String.valueOf(b), Toast.LENGTH_LONG).show();
            //turnOnLed(current.arduinoCode);
            MyApplication.getWritableDatabase().updateDatabase(current.id, "status", "on");
            //DisplayAll();
        }
        //
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
                toolbar.setSubtitle("Connected: " + name);
                //enable relativeLayout
            }
            progress.dismiss();
        }
    }
}
