package com.chen.freelancerspmapp.helper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.Entity.BusyTime;
import com.chen.freelancerspmapp.Entity.FreeTimeCard;
import com.chen.freelancerspmapp.Entity.WorkflowCard;
import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.Entity.Task;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

//    private List<Task> allTaskList = new ArrayList<>();
    private List<WorkflowCard> workflowLists = new ArrayList<>();
    private Context context;
    private List<BusyTime> busyTodo;
    private List<BusyTime> busyDoing;
    private List<BusyTime> busyDone;


    // Things needed to be set: task name,  planning date(start - due), actual date(start - due), actual spending time, timeline point date

    public TimeLineAdapter(List<WorkflowCard> cardList, List<BusyTime> busytodo, List<BusyTime> busydoing, List<BusyTime> busydone) {
        workflowLists = new ArrayList<>(cardList);
        busyTodo = busytodo;
        busyDoing = busydoing;
        busyDone = busydone;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.fragment_workflow, null);
        this.context = parent.getContext();
        return new TimeLineViewHolder(view, viewType);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        // Time line will show the workflow with actual start and finishing time
        String timelinePoint = "";
        String freeTimeText = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (workflowLists.size() > 0) {
            // This card is a task
            if (workflowLists.get(position).getTask() != null) {
                Task task = workflowLists.get(position).getTask();
                holder.itemTitle.setText(task.getName());
                String planningStartDate = simpleDateFormat.format(new Date(task.getPlanningStartDate()));

                // It's a to-do task
                if (task.getStatus() == 0) {
                    holder.materialCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    holder.materialCardView.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.stroke_width));
                    holder.materialCardView.setStrokeColor(Color.parseColor("#78909C"));
                    holder.taskType.setText("To Do");
                    timelinePoint = planningStartDate;

                   if( busyTodo.size()>0){
                       busyTodo.forEach(busyTime -> {
                           for (Long relatedTask : busyTime.getRelatedTasks()) {
                               if (relatedTask == task.getTaskID()) {
                                   if (task.getPlanningStartDate() >= busyTime.getDurationStart()) {
                                       holder.busyText.setText("You'll be busy between "+ busyTime.getDurationStartString()+" and " + busyTime.getDurationDueString());
                                       holder.busyCard.setVisibility(View.VISIBLE);
                                   }

                               }
                           }
                       });
                   }
                }
                // It's an in-progress task
                if (task.getStatus() == 1) {
                    holder.materialCardView.setCardBackgroundColor(Color.parseColor("#30A0E0"));
                    holder.taskType.setText("In Progress");
                    holder.taskType.setTextColor(Color.parseColor("#FFE3B3"));
                    holder.itemTitle.setTextColor(Color.parseColor("#FFE3B3"));
                    holder.materialCardView.setStrokeWidth(0);
                    String actualStartDate = simpleDateFormat.format(new Date(task.getActualStartDate()));
                    timelinePoint = actualStartDate;

                    if( busyDoing.size()>0){
                        busyDoing.forEach(busyTime -> {
                            for (Long relatedTask : busyTime.getRelatedTasks()) {
                                if (relatedTask == task.getTaskID()) {
                                    if (task.getActualStartDate() > busyTime.getDurationStart()) {
                                        holder.busyText.setText(String.format("You're busy between %s and %s", busyTime.getDurationStartString(), busyTime.getDurationDueString()));
                                        holder.busyCard.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        });
                    }

                }
                // It's a finished task
                if (task.getStatus() == 2) {
                    holder.materialCardView.setCardBackgroundColor(Color.parseColor("#97DBAE"));
                    holder.taskType.setText("Completed");
                    holder.taskType.setTextColor(Color.parseColor("#006BBB"));
                    holder.itemTitle.setTextColor(Color.parseColor("#006BBB"));
                    holder.materialCardView.setStrokeWidth(0);

                    String actualStartDate = simpleDateFormat.format(new Date(task.getActualStartDate()));
                    timelinePoint = actualStartDate;

                    if( busyDone.size()>0){
                        busyDone.forEach(busyTime -> {
                            for (Long relatedTask : busyTime.getRelatedTasks()) {
                                if (relatedTask == task.getTaskID()) {
                                    if (task.getActualStartDate() > busyTime.getDurationStart()) {
                                        holder.busyText.setText(String.format("You were busy between %s and %s", busyTime.getDurationStartString(), busyTime.getDurationDueString()));
                                        holder.busyCard.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        });
                    }

                }
                holder.pointDate.setText(timelinePoint.substring(0, timelinePoint.length() - 5));
            }

            // This card is for free time
            if (workflowLists.get(position).getFreeTimeCard() != null) {
                ViewGroup.LayoutParams freeTimeParams = holder.materialCardView.getLayoutParams();
                FreeTimeCard freeTimeCard = workflowLists.get(position).getFreeTimeCard();
                int freedays = freeTimeCard.getFreeDays();
                freeTimeText = freeTimeCard.getFreeTimeText();

                switch (freedays) {
                    case 2:
                        freeTimeParams.height = context.getResources().getDimensionPixelSize(R.dimen.text_view_height_2);
                        holder.timelineLayout.height = context.getResources().getDimensionPixelSize(R.dimen.timeline_height_short);
                        break;
                    case 3:
                    case 4:
                        freeTimeParams.height = context.getResources().getDimensionPixelSize(R.dimen.text_view_height_3_4);
                        holder.timelineLayout.height = context.getResources().getDimensionPixelSize(R.dimen.timeline_height);
                        break;
                    case 5:
                    case 6:
                    case 7:
                        freeTimeParams.height = context.getResources().getDimensionPixelSize(R.dimen.text_view_height_5_6_7);
                        holder.timelineLayout.height = context.getResources().getDimensionPixelSize(R.dimen.timeline_height);
                }
                if (freedays > 7) {
                    freeTimeParams.height = context.getResources().getDimensionPixelSize(R.dimen.text_view_height_8);
                    holder.timelineLayout.height = context.getResources().getDimensionPixelSize(R.dimen.timeline_height_long);
                }
                timelinePoint = freeTimeCard.getFreeStartToString();
                holder.pointDate.setText(timelinePoint.substring(0, timelinePoint.length() - 5));
                holder.taskType.setVisibility(View.GONE);
                holder.itemTitle.setText(freeTimeText);
                holder.materialCardView.setStrokeWidth(0);
                holder.materialCardView.setElevation(0);
                holder.materialCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                holder.itemTitle.setTextColor(Color.parseColor("#90A4AE"));
                holder.mTimelineView.setLineStyle(1);
                holder.materialCardView.setLayoutParams(freeTimeParams);
                holder.mTimelineView.setLayoutParams(holder.timelineLayout);
            }

        }


    }

    @Override
    public int getItemCount() {
        return workflowLists.size();
    }

    public void refreshTaskList(List<WorkflowCard> cardList, List<BusyTime> busytodo, List<BusyTime> busydoing, List<BusyTime> busydone){
        workflowLists.clear();
        workflowLists = new ArrayList<>(cardList);
        busyTodo = new ArrayList<>(busytodo);
        busyDoing = new ArrayList<>(busydoing);
        busyDone = new ArrayList<>(busydone);
    }

}
