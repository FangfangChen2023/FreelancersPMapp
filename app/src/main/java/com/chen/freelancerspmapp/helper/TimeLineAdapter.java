package com.chen.freelancerspmapp.helper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

    // Things needed to be set: task name,  planning date(start - due), actual date(start - due), actual spending time, timeline point date

    public TimeLineAdapter(List<WorkflowCard> cardList) {
        workflowLists = new ArrayList<>(cardList);
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
        String planningDate = "";
        String actualDate = "";
        String actualDuration = "";
        String timelinePoint = "";
        String freeTimeText = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (workflowLists.size() > 0) {
            // This card is a task
            if (workflowLists.get(position).getTask() != null) {
                Task task = workflowLists.get(position).getTask();
                holder.itemTitle.setText(task.getName());
                String planningStartDate = simpleDateFormat.format(new Date(task.getPlanningStartDate()));
                String planningDueDate = simpleDateFormat.format(new Date(task.getPlanningDueDate()));

                // It's a to-do task
                if (task.getStatus() == 0) {
                    holder.materialCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    holder.materialCardView.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.stroke_width));
                    holder.materialCardView.setStrokeColor(Color.parseColor("#78909C"));
                    holder.actualDate.setVisibility(View.GONE);
                    // Planning date setting
                    planningDate = planningStartDate + " - " + planningDueDate;
                    timelinePoint = planningStartDate;

                    long days = TimeUnit.MILLISECONDS.toDays(task.getPlanningDueDate() - task.getPlanningStartDate());
                    actualDuration = "  (" + days + " days)";
                }
                // It's an in-progress task
                if (task.getStatus() == 1) {
                    holder.tips.setText("In Progress");
                    holder.tips.setTextColor(Color.parseColor("#4A6572"));
                    holder.tips.setBackgroundColor(Color.parseColor("#F9AA33"));
                    holder.materialCardView.setCardBackgroundColor(Color.parseColor("#F9AA33"));
                    String actualStartDate = simpleDateFormat.format(new Date(task.getActualStartDate()));
                    actualDate = actualStartDate + " - ?";
                    long days = TimeUnit.MILLISECONDS.toDays(task.getPlanningDueDate() - task.getActualStartDate());
                    actualDuration = "  (" + days + " days)";
                    planningDate = planningStartDate + " - " + planningDueDate;
                    timelinePoint = actualStartDate;
                }
                // It's a finished task
                if (task.getStatus() == 2) {
                    holder.materialCardView.setCardBackgroundColor(Color.parseColor("#4A6572"));
                    holder.tips.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4A6572")));
                    holder.tips.setText("Done");
                    holder.tips.setTextColor(Color.parseColor("#F9AA33"));
                    holder.itemDate.setTextColor(Color.parseColor("#F9AA33"));
                    holder.actualDate.setTextColor(Color.parseColor("#F9AA33"));
                    holder.itemTitle.setTextColor(Color.parseColor("#F9AA33"));

                    String actualStartDate = simpleDateFormat.format(new Date(task.getActualStartDate()));
                    String actualDueDate = simpleDateFormat.format(new Date(task.getActualDueDate()));
                    actualDate = actualStartDate + " - " + actualDueDate;

                    planningDate = planningStartDate + " - " + planningDueDate;

                    // Actual spending time
                    long days = TimeUnit.MILLISECONDS.toDays(task.getActualDueDate() - task.getActualStartDate());
                    actualDuration = "  (" + days + " days)";

                    timelinePoint = actualStartDate;
                }
                holder.itemDate.setText("Planning time: \n" + planningDate);
                holder.actualDate.setText("Actual working time: \n" + actualDate + actualDuration);
                holder.pointDate.setText(timelinePoint.substring(0, timelinePoint.length() - 5));
            }

            // This card is for free time
            if (workflowLists.get(position).getFreeTimeCard() != null) {
                ViewGroup.LayoutParams freeTimeParams = holder.materialCardView.getLayoutParams();
                FreeTimeCard freeTimeCard = workflowLists.get(position).getFreeTimeCard();
                int freedays = freeTimeCard.getFreeDays();
                freeTimeText = freeTimeCard.getFreeTimeText();

                switch (freedays) {
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
                holder.tips.setVisibility(View.GONE);
                holder.itemDate.setVisibility(View.GONE);
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

    public void refreshTaskList(List<WorkflowCard> cardList) {
        workflowLists.clear();
        workflowLists = new ArrayList<>(cardList);
    }

}
