package com.chen.freelancerspmapp.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    TextView projItemView;
    ImageView projectImg;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        projItemView = itemView.findViewById(R.id.itemName);
        projectImg = itemView.findViewById(R.id.itemImage);
    }
}
