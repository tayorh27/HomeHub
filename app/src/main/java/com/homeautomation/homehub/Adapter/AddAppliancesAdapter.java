package com.homeautomation.homehub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.information.Appliance;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 17-Jul-16.
 */
public class AddAppliancesAdapter extends RecyclerView.Adapter<AddAppliancesAdapter.AppliancesAdapter> {

    Context context;
    ArrayList<Appliance> appliances = new ArrayList<>();
    LayoutInflater inflater;

    public AddAppliancesAdapter(Context context) {
        this.context = context;
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
        holder.tv.setText(current.name);
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

    public class AppliancesAdapter extends RecyclerView.ViewHolder {

        TextView tv;
        public AppliancesAdapter(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.textView);
        }
    }
}
