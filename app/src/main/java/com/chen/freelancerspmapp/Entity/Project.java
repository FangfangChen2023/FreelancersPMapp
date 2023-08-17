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
    private Long planningStartDate;
    private Long planningDueDate;
    private int status; // 0: current; 1: history
    @Nullable
    private Long actualStartDate;
    @Nullable
    private Long actualDueDate;

//    public Project() {
//    }

    public Project(String name, String details, Long planningStartDate, Long planningDueDate, int status) {
        this.name = name;
        this.planningDueDate = planningDueDate;
        this.planningStartDate = planningStartDate;
        this.status = status;
        this.details = details;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public Long getPlanningStartDate() {
        return planningStartDate;
    }

    public void setPlanningStartDate(Long planningStartDate) {
        this.planningStartDate = planningStartDate;
    }

    public Long getPlanningDueDate() {
        return planningDueDate;
    }

    public void setPlanningDueDate(Long planningDueDate) {
        this.planningDueDate = planningDueDate;
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

    @Nullable
    public Long getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(@Nullable Long actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    @Nullable
    public Long getActualDueDate() {
        return actualDueDate;
    }

    public void setActualDueDate(@Nullable Long actualDueDate) {
        this.actualDueDate = actualDueDate;
    }
}
