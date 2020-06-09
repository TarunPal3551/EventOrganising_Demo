package com.example.eventorganising_demo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context mcontext;
    private ArrayList<Event> eventArrayList;


    public HistoryAdapter(Context mcontext, ArrayList<Event> eventArrayList) {

        this.mcontext = mcontext;
        this.eventArrayList = eventArrayList;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewDate, textViewTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);


        }


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(eventArrayList.get(position).getName());
        holder.textViewDate.setText(eventArrayList.get(position).getDate());
        holder.textViewTime.setText(eventArrayList.get(position).getTime());
    }


    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }


}
