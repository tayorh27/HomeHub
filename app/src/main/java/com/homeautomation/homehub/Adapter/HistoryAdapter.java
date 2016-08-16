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
import com.homeautomation.homehub.information.History;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 04-Aug-16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HAdapter> {

    Context context;
    ArrayList<History> hist = new ArrayList<>();
    LayoutInflater inflater;
    private OnClickListener clickListener;

    public HistoryAdapter(Context context, OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    public void FillAppliance(ArrayList<History> hist) {
        this.hist = hist;
        notifyDataSetChanged();
    }

    @Override
    public HAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_history_layout, parent, false);
        HAdapter adapter = new HAdapter(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(HAdapter holder, int position) {
        History h = hist.get(position);
        holder.dt.setText(h.dateTime);
        holder.history.setText(h.history);
    }

    @Override
    public int getItemCount() {
        return hist.size();
    }

    public class HAdapter extends RecyclerView.ViewHolder{

        TextView dt,history;
        ImageButton delete;
        public HAdapter(View itemView) {
            super(itemView);
            dt = (TextView)itemView.findViewById(R.id.tv_dateTime);
            history = (TextView)itemView.findViewById(R.id.textView);
            delete = (ImageButton)itemView.findViewById(R.id.remove);

            delete.setOnClickListener(new View.OnClickListener() {
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
