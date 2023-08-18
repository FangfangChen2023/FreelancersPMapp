package com.chen.freelancerspmapp.viewmodel;

import com.chen.freelancerspmapp.Entity.Task;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class CustomComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        long day1 = 0;
        long day2 = 0;
        // sorting to do tasks
        if(task1.getStatus()==0){
             day1 = TimeUnit.MILLISECONDS.toDays(task1.getPlanningStartDate());
             day2 = TimeUnit.MILLISECONDS.toDays(task2.getPlanningStartDate());
        }
        // sorting doing tasks and done tasks
        if(task1.getStatus()==1 || task1.getStatus()==2){
             day1 = TimeUnit.MILLISECONDS.toDays(task1.getActualStartDate());
             day2 = TimeUnit.MILLISECONDS.toDays(task2.getActualStartDate());
        }
        // sorting done tasks
//        if(task1.getStatus()==2){
//            day1 = TimeUnit.MILLISECONDS.toDays(task1.getActualDueDate());
//            day2 = TimeUnit.MILLISECONDS.toDays(task2.getActualDueDate());
//        }
        return Integer.compare(Math.round(day1), Math.round(day2));
    }
}
