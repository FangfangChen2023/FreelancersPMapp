package com.chen.freelancerspmapp.helper;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.OnMoreActionButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class CompletedRecyclerAdapter extends RecyclerView.Adapter<CompletedViewHolder> {
    private List<Task> completedTaskList = new ArrayList<>();
    private OnMoreActionButtonClickListener buttonClickListener;

    public CompletedRecyclerAdapter(List<Task> completedTaskList) {
        this.completedTaskList = new ArrayList<>(completedTaskList);
    }

    public void setOnMoreActionBtnClickListener(OnMoreActionButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompletedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (completedTaskList != null && completedTaskList.size() > 0) {
            holder.completedItemView.setText(completedTaskList.get(position).getName());
            holder.completedImage.setImageResource(R.drawable.baseline_check_circle_24);
            holder.itemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu moreActionsPopupMenu = new PopupMenu(v.getContext(), holder.itemDetails);
                    moreActionsPopupMenu.getMenuInflater().inflate(R.menu.more_actions, moreActionsPopupMenu.getMenu());
                    moreActionsPopupMenu.getMenu().removeItem(R.id.state_changeto_doing);
                    moreActionsPopupMenu.getMenu().removeItem(R.id.state_changeto_done);
                    if (buttonClickListener != null) {
                        buttonClickListener.onButtonClick(position, moreActionsPopupMenu);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return completedTaskList.size();
    }

    public void refreshDoneTaskList(List<Task> taskList) {
        Log.d("Refreshing", "----------------------------------------------------------------");
        completedTaskList.clear();
        completedTaskList = new ArrayList<>(taskList);
    }
}
