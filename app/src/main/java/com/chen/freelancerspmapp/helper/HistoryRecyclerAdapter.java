package com.chen.freelancerspmapp.helper;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.Entity.Project;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    List<Project> historyProjectList = new ArrayList<>();

    public HistoryRecyclerAdapter(List<Project> historyProjectList) {
        this.historyProjectList = historyProjectList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        if(historyProjectList.size()>0){
            holder.projItemView.setText(historyProjectList.get(position).getName());
            holder.projectImg.setImageResource(R.drawable.baseline_check_circle_24);
        }

    }

    @Override
    public int getItemCount() {
        return historyProjectList.size();
    }
}
