package com.chen.freelancerspmapp.helper;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.Entity.Task;

import java.util.List;

public class InprogressRecyclerAdapter extends RecyclerView.Adapter<InprogressViewHolder> {
    List<Task> ongoingTaskList;

    public InprogressRecyclerAdapter(List<Task> ongoingTaskList) {
        this.ongoingTaskList = ongoingTaskList;
    }

    @NonNull
    @Override
    public InprogressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InprogressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InprogressViewHolder holder, int position) {
        holder.ongoingItemView.setText(ongoingTaskList.get(position).getName());
        holder.ongoingImage.setImageResource(R.drawable.baseline_hourglass_top_24);
    }

    @Override
    public int getItemCount() {
        return ongoingTaskList.size();
    }
}
