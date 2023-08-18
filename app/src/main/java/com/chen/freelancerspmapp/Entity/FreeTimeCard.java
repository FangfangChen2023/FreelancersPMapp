package com.chen.freelancerspmapp.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// Not interacting with database
public class FreeTimeCard {
    private int freeDays;
    private String freeTimeText;
    // In MILLISECONDS
    private long freeStart;
    // In MILLISECONDS
    private long freeEnd;

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

    public long getFreeStart() {
        return freeStart;
    }

    public void setFreeStart(long freeStart) {
        this.freeStart = freeStart;
    }

    public long getFreeEnd() {
        return freeEnd;
    }

    public void setFreeEnd(long freeEnd) {
        this.freeEnd = freeEnd;
    }

    public String getFreeStartToString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(new Date(freeStart));
    }
}
