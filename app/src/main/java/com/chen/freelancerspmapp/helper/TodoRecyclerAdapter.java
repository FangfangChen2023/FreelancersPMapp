package com.chen.freelancerspmapp.helper;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.OnMoreActionButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerViewHolder> {
     private List<Task> todoList;
    private OnMoreActionButtonClickListener buttonClickListener;
    public TodoRecyclerAdapter(List<Task> taskList) {

        todoList = new ArrayList<>(taskList);
    }

    public void setOnMoreActionBtnClickListener(OnMoreActionButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    @NonNull
    @Override
    public TaskRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(todoList != null &&  todoList.size() >0){
            holder.itemName.setText(todoList.get(position).getName());
            holder.imageView.setImageResource(R.drawable.baseline_access_time_24);
            holder.planningDate.setText(todoList.get(position).getPlanningDateToString());
            holder.planningDate.setTextColor(Color.parseColor("#344955"));
            holder.itemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu moreActionsPopupMenu = new PopupMenu(v.getContext(), holder.itemDetails);
                    moreActionsPopupMenu.getMenuInflater().inflate(R.menu.more_actions, moreActionsPopupMenu.getMenu());
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
        if(todoList != null && todoList.size() > 0){
            return todoList.size();
        }
       return 0;
    }

    public void refreshTodoTaskList(List<Task> taskList){
        todoList.clear();
        todoList = new ArrayList<>(taskList);
//        todoList.forEach(task -> {
//            Log.d("inside adapter:", task.getName());
//        });
    }
}
