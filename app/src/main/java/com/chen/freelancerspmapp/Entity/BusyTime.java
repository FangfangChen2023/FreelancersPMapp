package com.chen.freelancerspmapp.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.apache.commons.lang3.Range;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Entity(tableName = "busytime_table")
public class BusyTime {
//    @PrimaryKey(autoGenerate = true)
    private long busyTimeID;
    private long durationStart;
    private long durationEnd;
    private long projectID;
    private List<Long> relatedTaskIDs = new ArrayList<>();

    public BusyTime(long projectID, long durationStart, long durationEnd) {
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
        this.projectID = projectID;
    }

    public long getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(long durationStart) {
        this.durationStart = durationStart;
    }

    public long getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(long durationEnd) {
        this.durationEnd = durationEnd;
    }

    public String getDurationStartString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String temp = simpleDateFormat.format(new Date(durationStart));
        return temp;
    }
    public String getDurationDueString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String temp = simpleDateFormat.format(new Date(durationEnd));
        return temp;
    }

    public void setRelatedTaskIDs(List<Long> relatedTaskIDs) {
        this.relatedTaskIDs = relatedTaskIDs;
    }

    public List<Long> getRelatedTasks() {
        return relatedTaskIDs;
    }
    public void addRelatedTask(long taskID){
        relatedTaskIDs.add(taskID);
    }
    public boolean isOverlappedBy(long lowValue, long highValue){
        Range oldPlan = Range.between(durationStart, durationEnd);
        Range newPlan = Range.between(lowValue, highValue);
        return oldPlan.isOverlappedBy(newPlan);
    }
}
