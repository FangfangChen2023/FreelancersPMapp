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

public class CompletedRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerViewHolder> {
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
    public TaskRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (completedTaskList != null && completedTaskList.size() > 0) {
            holder.itemName.setText(completedTaskList.get(position).getName());
            holder.imageView.setImageResource(R.drawable.baseline_check_circle_24);
            holder.planningDate.setText(completedTaskList.get(position).getPlanningDateToString());
            holder.actualDate.setVisibility(View.VISIBLE);
            holder.actualDate.setText(completedTaskList.get(position).getActualDateToString());
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
        if(completedTaskList != null && completedTaskList.size() > 0){
            return completedTaskList.size();
        }
        return 0;
    }

    public void refreshDoneTaskList(List<Task> taskList) {
        completedTaskList.clear();
        completedTaskList = new ArrayList<>(taskList);
    }
}
