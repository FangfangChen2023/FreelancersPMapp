package com.chen.freelancerspmapp.helper;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;

public class CompletedViewHolder extends RecyclerView.ViewHolder {
    TextView completedItemView;
    ImageView completedImage;
    Button itemDetails;

    public CompletedViewHolder(@NonNull View itemView) {
        super(itemView);
        completedItemView = itemView.findViewById(R.id.itemName);
        completedImage = itemView.findViewById(R.id.itemImage);
        itemDetails = itemView.findViewById(R.id.itemDetails);
    }
}
