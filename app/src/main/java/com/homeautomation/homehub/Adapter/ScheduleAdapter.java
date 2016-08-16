package com.homeautomation.homehub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homeautomation.homehub.R;
import com.homeautomation.homehub.callbacks.OnClickListener;
import com.homeautomation.homehub.information.Schedule;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 07-Aug-16.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MySchedule> {

    Context context;
    ArrayList<Schedule> schedules = new ArrayList<>();
    LayoutInflater inflater;
    private OnClickListener clickListener;

    public ScheduleAdapter(Context context, OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void FillSchedule(ArrayList<Schedule> hist) {
        this.schedules = hist;
        notifyDataSetChanged();
    }

    @Override
    public MySchedule onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_schedule_layout, parent, false);
        MySchedule adapter = new MySchedule(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(MySchedule holder, int position) {
        Schedule current = schedules.get(position);
        holder.app_name.setText(current.app_name);
        holder.app_status.setText(current.nStatus);
        holder.app_mode.setText("Mode set to: "+current.app_status);
        holder.app_date.setText("Schedule at: "+current.datetime);
        holder.app_length.setText("Timeout: "+current.length);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class MySchedule extends RecyclerView.ViewHolder {

        TextView app_name, app_mode, app_date, app_status, app_length;
        ImageButton dl;

        public MySchedule(View itemView) {
            super(itemView);
            app_name = (TextView)itemView.findViewById(R.id.sapp_name);
            app_mode = (TextView)itemView.findViewById(R.id.sapp_mode);
            app_date = (TextView)itemView.findViewById(R.id.sapp_dt);
            app_status = (TextView)itemView.findViewById(R.id.sapp_nstatus);
            app_length = (TextView)itemView.findViewById(R.id.sapp_length);
            dl = (ImageButton)itemView.findViewById(R.id.remove);

            dl.setOnClickListener(new View.OnClickListener() {
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
