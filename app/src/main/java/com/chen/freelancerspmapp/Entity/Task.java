package com.chen.freelancerspmapp.Entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private Long taskID;
    @NotNull
    private Long projectID;
    private String name;
    @Nullable
    private String detail;
    private Long planningDueDate;
    private Long planningStartDate;
    private int status; // 0: to do; 1: in progress; 2: completed

    @Nullable
    private Long actualStartDate;
    @Nullable
    private Long actualDueDate;

//    public Task() {
//    }


    public Task(Long projectID, String name, String detail, Long planningStartDate, Long planningDueDate, int status) {
        this.projectID = projectID;
        this.name = name;
        this.detail = detail;
        this.planningDueDate = planningDueDate;
        this.status = status;
        this.planningStartDate = planningStartDate;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDetail() {
        return detail;
    }

    public void setDetail(@Nullable String detail) {
        this.detail = detail;
    }

    public Long getPlanningDueDate() {
        return planningDueDate;
    }

    public void setPlanningDueDate(Long dueDate) {
        this.planningDueDate = dueDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getPlanningStartDate() {
        return planningStartDate;
    }

    public void setPlanningStartDate(Long planningStartDate) {
        this.planningStartDate = planningStartDate;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public Long getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Long actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Long getActualDueDate() {
        return actualDueDate;
    }

    public void setActualDueDate(Long actualDueDate) {
        this.actualDueDate = actualDueDate;
    }

    public String getPlanningDateToString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = simpleDateFormat.format(new Date(planningStartDate));
        String dueDate = simpleDateFormat.format(new Date(planningDueDate));

        long days = TimeUnit.MILLISECONDS.toDays(planningDueDate - planningStartDate);
        String planningDuration = "  (" + days + " days)";

        return "Planning Date:  " + startDate + " - " + dueDate + planningDuration;
    }

    public String getActualDateToString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = simpleDateFormat.format(new Date(actualStartDate));
        String dueDate = simpleDateFormat.format(new Date(actualDueDate));

        long days = TimeUnit.MILLISECONDS.toDays(actualDueDate - actualStartDate);
        String actualDuration = "  (" + days + " days)";

        return "Actual Date:    " + startDate + " - " + dueDate + actualDuration;
    }
}
