package com.homeautomation.homehub.Adapter.HubSettings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.SettingsCheckListener;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 02-Aug-16.
 */
public class HubSettingsAdapter extends RecyclerView.Adapter<HubSettingsAdapter.SettingsAdapter> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Appliance> data = new ArrayList<>();
    SettingsCheckListener checkListener;
    String page;

    public HubSettingsAdapter(Context context, SettingsCheckListener checkListener,String page) {
        this.context = context;
        this.checkListener = checkListener;
        this.page = page;
        inflater = LayoutInflater.from(context);
    }

    public void FillRecyclerView(ArrayList<Appliance> dt) {
        this.data = dt;
        notifyDataSetChanged();
    }

    @Override
    public SettingsAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_settings_all, parent, false);
        SettingsAdapter settingsAdapter = new SettingsAdapter(view);
        return settingsAdapter;
    }

    @Override
    public void onBindViewHolder(SettingsAdapter holder, int position) {
        Appliance current = data.get(position);
        if (!current.name.contentEquals("none") && !current.arduinoCode.contentEquals("")) {
            holder.tv.setText(current.name + " - " + current.arduinoCode);
            if(page.contentEquals("high")){

            }else if(page.contentEquals("balanced")){
                if(current.balanced){
                    holder.cb.setChecked(true);
                }else {
                    holder.cb.setChecked(false);
                }
            }else if(page.contentEquals("saver")){

            }
            //holder.view.setBackgroundColor(context.getResources().getColor(bg));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SettingsAdapter extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView tv;
        CheckBox cb;

        public SettingsAdapter(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.custom_relative);
            tv = (TextView) itemView.findViewById(R.id.settings_tv);
            cb = (CheckBox) itemView.findViewById(R.id.settings_checkBox);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkListener != null) {
                        checkListener.onSettingsRelativeCheckListener(cb, cb.isChecked(), getPosition());
                    }
                }
            });

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (checkListener != null) {
                        checkListener.onSettingsCheckListener(compoundButton, b, getPosition());
                    }
                }
            });
        }
    }
}
