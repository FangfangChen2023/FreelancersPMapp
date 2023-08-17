package com.chen.freelancerspmapp.Entity;

// Not interacting with database
public class FreeTimeCard {
    private int freeDays;
    private String freeTimeText;

    public FreeTimeCard() {
    }

    public FreeTimeCard(int freeDays, String freeTimeText) {
        this.freeDays = freeDays;
        this.freeTimeText = freeTimeText;
    }

    public int getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(int freeDays) {
        this.freeDays = freeDays;
    }

    public String getFreeTimeText() {
        return freeTimeText;
    }

    public void setFreeTimeText(String freeTimeText) {
        this.freeTimeText = freeTimeText;
    }
}
