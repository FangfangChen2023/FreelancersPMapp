package com.chen.freelancerspmapp.helper;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.viewmodel.OnMoreActionButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoingRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerViewHolder>{
    private List<Task> doingTaskList;
    private OnMoreActionButtonClickListener buttonClickListener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DoingRecyclerAdapter(List<Task> doingTaskList) {
        this.doingTaskList = new ArrayList<>(doingTaskList);
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
        if(doingTaskList != null && doingTaskList.size() != 0){
            holder.itemName.setText(doingTaskList.get(position).getName());
            holder.imageView.setImageResource(R.drawable.baseline_update_24);
            holder.planningDate.setText(doingTaskList.get(position).getPlanningDateToString());
            String startDate = simpleDateFormat.format(new Date(doingTaskList.get(position).getActualStartDate()));
            holder.actualDate.setVisibility(View.VISIBLE);
            holder.actualDate.setText("Actual start Date:   "+startDate);
            holder.itemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu moreActionsPopupMenu = new PopupMenu(v.getContext(), holder.itemDetails);
                    moreActionsPopupMenu.getMenuInflater().inflate(R.menu.more_actions, moreActionsPopupMenu.getMenu());
                    moreActionsPopupMenu.getMenu().removeItem(R.id.state_changeto_doing);

                    if (buttonClickListener != null) {
                        buttonClickListener.onButtonClick(position, moreActionsPopupMenu);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(doingTaskList != null && doingTaskList.size() != 0){
            return doingTaskList.size();
        }
        return 0;
    }

    public void refreshDoingTaskList(List<Task> taskList){
        doingTaskList.clear();
        doingTaskList = new ArrayList<>(taskList);
//        todoList.forEach(task -> {
//            Log.d("inside adapter:", task.getName());
//        });
    }
}
