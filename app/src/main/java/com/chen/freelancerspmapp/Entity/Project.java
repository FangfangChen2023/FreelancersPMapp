package com.chen.freelancerspmapp.Entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "project_table")
public class Project {
    @PrimaryKey(autoGenerate = true)
    private Long projectID;
    private String name;
    @Nullable
    private String details;
    private String startDate;
    private String dueDate;

    private int status; // 0: current; 1: history

//    public Project() {
//    }

    public Project(String name, String details, String startDate, String dueDate, int status) {
        this.name = name;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.status = status;
        this.details = details;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDetails() {
        return details;
    }

    public void setDetails(@Nullable String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
