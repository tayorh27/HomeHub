package com.homeautomation.homehub.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.homeautomation.homehub.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnect extends AppCompatActivity {

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    public static String EXTRA_NAME = "device_name";
    ListView listView;
    LinearLayout devices;

    private ProgressDialog progress;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        listView = (ListView)findViewById(R.id.listView);
        devices = (LinearLayout)findViewById(R.id.noLayout);

        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }else {
            pairedDevicesList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetooth_connect,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            pairedDevicesList();
        }
        return super.onOptionsItemSelected(item);
    }

    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
            devices.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            String name = info.substring(0,info.length() - 17);

            //new ConnectBT(address).execute();

             //Make an intent to start next activity.
            Intent i = new Intent(BluetoothConnect.this, HomeHub.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            i.putExtra(EXTRA_NAME,name);
            startActivity(i);
            finish();
        }
    };

    private void msg(String s) {
        Toast.makeText(BluetoothConnect.this, s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {//UI THREAD
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        String address;
        public ConnectBT(String address){
            this.address = address;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BluetoothConnect.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                //String id = UUID.randomUUID().toString();;;;1b9a4de0-52be-11e6-bdf4-0800200c9a66
                UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//"00001101-0000-1000-8000-00805F9B34FB"
                if (btSocket == null || !isBtConnected) {
                //Rfcomm d;
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
                 }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                Log.e("Bluetooth - Error",e.toString());
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
                //toolbar.setSubtitle("Connected: " + name);
            }
            progress.dismiss();
        }
    }
}
