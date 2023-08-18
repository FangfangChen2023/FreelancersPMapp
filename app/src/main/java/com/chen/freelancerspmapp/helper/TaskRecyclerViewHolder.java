package com.chen.freelancerspmapp.helper;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;

public class TaskRecyclerViewHolder extends RecyclerView.ViewHolder{
     TextView itemName;
     ImageView imageView;
    Button itemDetails;
    TextView planningDate;
    TextView actualDate;

    public TaskRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName);
        imageView = itemView.findViewById(R.id.itemImage);
        itemDetails = itemView.findViewById(R.id.itemDetails);
        planningDate = itemView.findViewById(R.id.planning_time);
        actualDate = itemView.findViewById(R.id.actual_date);
    }
}
