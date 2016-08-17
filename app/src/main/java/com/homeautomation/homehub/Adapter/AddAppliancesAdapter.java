package com.homeautomation.homehub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnClickListener;
import com.homeautomation.homehub.information.Appliance;
import com.homeautomation.homehub.utility.General;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class AddAppliancesAdapter extends RecyclerView.Adapter<AddAppliancesAdapter.AppliancesAdapter> {

    Context context;
    ArrayList<Appliance> appliances = new ArrayList<>();
    LayoutInflater inflater;
    private OnClickListener clickListener;

    public AddAppliancesAdapter(Context context, OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void FillAppliance(ArrayList<Appliance> appliances) {
        this.appliances = appliances;
        notifyDataSetChanged();
    }



    @Override
    public AppliancesAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.appliance_list, parent, false);
        AppliancesAdapter adapter = new AppliancesAdapter(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(AppliancesAdapter holder, int position) {
        Appliance current = appliances.get(position);
        if(!current.name.contentEquals("none") && !current.arduinoCode.contentEquals("")) {
            int bg = General.getColor(current.color);
            holder.tv.setText(current.name+" - "+current.arduinoCode);
            holder.tv.setTextColor(context.getResources().getColor(bg));
            holder.view.setBackgroundColor(context.getResources().getColor(bg));
        }
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

    public class AppliancesAdapter extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView view,remove;
        public AppliancesAdapter(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.textView);
            view = (ImageView)itemView.findViewById(R.id.app_color);
            remove = (ImageView)itemView.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null){
                        clickListener.clicked(view,getPosition());
                    }
                }
            });
        }
    }
}
