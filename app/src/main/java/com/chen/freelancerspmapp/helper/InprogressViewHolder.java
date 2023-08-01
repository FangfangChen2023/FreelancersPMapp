package com.chen.freelancerspmapp.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;

public class InprogressViewHolder extends RecyclerView.ViewHolder{

    TextView ongoingItemView;
    ImageView ongoingImage;

    public InprogressViewHolder(@NonNull View itemView) {
        super(itemView);
        ongoingItemView = itemView.findViewById(R.id.itemName);
        ongoingImage = itemView.findViewById(R.id.itemImage);
    }
}
