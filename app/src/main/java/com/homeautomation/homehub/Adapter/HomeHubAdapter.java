package com.homeautomation.homehub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnCheckChangeListener;
import com.homeautomation.homehub.information.Appliance;
import com.homeautomation.homehub.utility.General;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 29-Jul-16.
 */
public class HomeHubAdapter extends RecyclerView.Adapter<HomeHubAdapter.HubAdapter>{

    Context context;
    ArrayList<Appliance> appliances = new ArrayList<>();
    LayoutInflater inflater;
    private OnCheckChangeListener checkListener;

    public HomeHubAdapter(Context context, OnCheckChangeListener checkListener) {
        this.context = context;
        this.checkListener = checkListener;
        inflater = LayoutInflater.from(context);
    }

    public void FillAppliance(ArrayList<Appliance> appliances) {
        this.appliances = appliances;
        notifyDataSetChanged();
    }

    @Override
    public HubAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_hub_layout, parent, false);
        HubAdapter adapter = new HubAdapter(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(HubAdapter holder, int position) {
        Appliance current = appliances.get(position);
        if(!current.name.contentEquals("none") && !current.arduinoCode.contentEquals("")) {
            int bg = General.getColor(current.color);
            holder.tv.setText(current.name +" - "+ current.status);
            //holder.tv.setTextColor(context.getResources().getColor(bg));
            holder.aSwitch.setChecked(getStatus(current.status));
            holder.aSwitch.setText(getConnection(current.status));
            //holder.aSwitch.setTextColor(context.getResources().getColor(bg));
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(bg));
        }
    }

    public boolean getStatus(String status){
        if(status.contentEquals("on")){
            return true;
        }else {
            return false;
        }
    }

    public String getConnection(String status){
        if(status.contentEquals("on")){
            return "Connected";
        }else {
            return "Disconnected";
        }
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

    public class HubAdapter extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView tv;
        Switch aSwitch;
        RelativeLayout relativeLayout;

        public HubAdapter(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.a_name_connected);
            iv = (ImageView)itemView.findViewById(R.id.a_image);
            aSwitch = (Switch)itemView.findViewById(R.id.a_switch1);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.hub_layout);
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkListener.checkChange(compoundButton,b,getPosition());
                }
            });
        }
    }
}
