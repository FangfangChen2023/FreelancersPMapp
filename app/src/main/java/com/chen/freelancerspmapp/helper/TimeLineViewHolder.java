package com.chen.freelancerspmapp.helper;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chen.freelancerspmapp.R;
import com.github.vipulasri.timelineview.TimelineView;

public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    public TimelineView mTimelineView;
    TextView itemTitle;
    TextView itemDate;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
       itemTitle = itemView.findViewById(R.id.text_timeline_title);
       itemDate = itemView.findViewById(R.id.text_timeline_date);
        mTimelineView.initLine(viewType);
    }
}
