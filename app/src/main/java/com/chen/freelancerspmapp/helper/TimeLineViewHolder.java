package com.chen.freelancerspmapp.helper;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    public TimelineView mTimelineView;
    TextView itemTitle;
//    TextView itemDate;
//    TextView actualDate;
    TextView pointDate;
    TextView taskType;
    MaterialCardView materialCardView;
    ViewGroup.LayoutParams timelineLayout;
//    TextView tips;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
        itemTitle = itemView.findViewById(R.id.text_timeline_title);
//        itemDate = itemView.findViewById(R.id.planning_time);
//        actualDate = itemView.findViewById(R.id.actual_date);
        pointDate = itemView.findViewById(R.id.point_date);
        materialCardView = itemView.findViewById(R.id.detail_card);
        taskType = itemView.findViewById(R.id.task_type);
//        tips = itemView.findViewById(R.id.tips);
        timelineLayout = mTimelineView.getLayoutParams();
        mTimelineView.initLine(viewType);
    }
}


