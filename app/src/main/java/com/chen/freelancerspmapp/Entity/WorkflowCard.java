package com.chen.freelancerspmapp.Entity;

public class WorkflowCard {
    private Task task;
    private FreeTimeCard freeTimeCard;
    private BusyTime busyTime;

    public WorkflowCard() {
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public FreeTimeCard getFreeTimeCard() {
        return freeTimeCard;
    }

    public void setFreeTimeCard(FreeTimeCard freeTimeCard) {
        this.freeTimeCard = freeTimeCard;
    }

    public BusyTime getBusyTime() {
        return busyTime;
    }

    public void setBusyTime(BusyTime busyTime) {
        this.busyTime = busyTime;
    }
}
