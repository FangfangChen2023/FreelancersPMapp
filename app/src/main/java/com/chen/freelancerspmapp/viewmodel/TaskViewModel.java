package com.chen.freelancerspmapp.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chen.freelancerspmapp.Entity.BusyTime;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.Repository.RepositoryCallback;
import com.chen.freelancerspmapp.Repository.TaskRepositoryImpl;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepositoryImpl taskRepository;
    private LiveData<List<Task>> allTasks = new MutableLiveData<>();
    private static MutableLiveData<List<Task>> workflowTasks = new MutableLiveData<>();
    private MutableLiveData<List<Task>> todoTasks = new MutableLiveData<>();
    private static MutableLiveData<List<Task>> doingTasks = new MutableLiveData<>();
    private static MutableLiveData<List<Task>> doneTasks = new MutableLiveData<>();
    private CustomComparator taskComparator = new CustomComparator();
    private List<BusyTime> busyInTodo = new ArrayList<>();
    private List<BusyTime> busyInDoing = new ArrayList<>();
    private List<BusyTime> busyInDone = new ArrayList<>();
    private long projectID;
    private Application application;

    public TaskViewModel(@NonNull Application application, Long projectID) {
        super(application);
        this.application = application;
        this.projectID = projectID;
        taskRepository = new TaskRepositoryImpl(application);
        initializeLists(projectID);
    }

    public void initializeLists(Long projectID) {
        try {
            getAllTasksFromDB(projectID).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        categorizeTasks();
    }

    public Future getAllTasksFromDB(Long projectID) {
        Future future = taskRepository.queryAllTasks(projectID, new RepositoryCallback() {
            @Override
            public void onComplete(LiveData queryResult) {
                allTasks = queryResult;
            }
        });
        return future;
    }

    public LiveData<List<Task>> getAllTasks() {
        return workflowTasks;
    }

    public void categorizeTasks() {
        List<Task> todoTemp = new ArrayList<>();
        List<Task> doingTemp = new ArrayList<>();
        List<Task> doneTemp = new ArrayList<>();
        if (allTasks.getValue() != null && allTasks.getValue().size() > 0) {
            allTasks.getValue().forEach((task) -> {
                if (task.getStatus() == 0) {
                    todoTemp.add(task);
                }
                if (task.getStatus() == 1) {
                    doingTemp.add(task);

                }
                if (task.getStatus() == 2) {
                    doneTemp.add(task);
                }
            });
        }
        // Sorting tasks according to their start time, due time and actual start and finishing time
        todoTemp.sort(taskComparator);
        todoTasks.setValue(todoTemp);

        doingTemp.sort(taskComparator);
        doingTasks.setValue(doingTemp);

        doneTemp.sort(taskComparator);
        doneTasks.setValue(doneTemp);

        checkHeavyWorkload();

        List<Task> allTasksTemp = new ArrayList<>();
        allTasksTemp.addAll(doneTemp);
        allTasksTemp.addAll(doingTemp);
        allTasksTemp.addAll(todoTemp);
        workflowTasks.setValue(allTasksTemp);
    }

    public LiveData<Task> getTask(Long taskID, Long projectID) {
        final LiveData<Task>[] task = new LiveData[]{new MutableLiveData()};
        taskRepository.queryTask(taskID, projectID, new RepositoryCallback() {
            @Override
            public void onComplete(LiveData queryResult) {
                task[0] = queryResult;
            }
        });
        return task[0];
    }

    public LiveData<List<Task>> getToDoTasks() {
        return todoTasks;
    }

    public LiveData<List<Task>> getInProgressTasks() {
        return doingTasks;
    }

    public LiveData<List<Task>> getDoneTasks() {
        return doneTasks;
    }

    public void deleteTask(Task task) {
        try {
            taskRepository.deleteTask(task).get();
            getAllTasksFromDB(task.getProjectID()).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        categorizeTasks();
    }

    public void insertTask(Task task) {
        try {
            taskRepository.insertTask(task).get();
            getAllTasksFromDB(task.getProjectID()).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        categorizeTasks();
    }

    public void updateTask(Task task) {
        try {
            taskRepository.updateTask(task).get();
            getAllTasksFromDB(task.getProjectID()).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        categorizeTasks();
    }

    public void checkHeavyWorkload() {
        busyInTodo.clear();
        busyInDoing.clear();
        busyInDone.clear();

        // to do
        List<Task> todoList = todoTasks.getValue();
        for (int i = 0; i < todoList.size() - 1; i++) {
            Range<Long> planningRange = Range.between(todoList.get(i).getPlanningStartDate(), todoList.get(i).getPlanningDueDate());
            Range<Long> nextPlanningRange = Range.between(todoList.get(i + 1).getPlanningStartDate(), todoList.get(i + 1).getPlanningDueDate());
            if (!planningRange.isOverlappedBy(nextPlanningRange)) {
                break;
            }
            // To do list has been sorted by planning start date before
            // So i.getPlanningStartDate < (i+1).getPlanningStartDate
            long rangeStart = todoList.get(i + 1).getPlanningStartDate();
            long rangeEnd =
                    todoList.get(i).getPlanningDueDate() < todoList.get(i + 1).getPlanningDueDate() ?
                            todoList.get(i).getPlanningDueDate() : todoList.get(i + 1).getPlanningDueDate();

            BusyTime busyTime = new BusyTime(projectID, rangeStart, rangeEnd);
            busyTime.addRelatedTask(todoList.get(i).getTaskID());
            busyTime.addRelatedTask(todoList.get(i+1).getTaskID());
            busyInTodo.add(busyTime);
        }
        // doing

        List<Task> doingList = doingTasks.getValue();
        for (int i = 0; i < doingList.size() - 1; i++) {
            Range<Long> actualRange = Range.between(doingList.get(i).getActualStartDate(), doingList.get(i).getPlanningDueDate());
            Range<Long> nextActualRange = Range.between(doingList.get(i + 1).getActualStartDate(), doingList.get(i + 1).getPlanningDueDate());
            if (!actualRange.isOverlappedBy(nextActualRange)) {
                break;
            }
            // Done list has been sorted by actual start date before
            // So i.getActualStartDate < (i+1).getActualStartDate
            long rangeStart = doingList.get(i + 1).getActualStartDate();
            long rangeEnd =
                    doingList.get(i).getPlanningDueDate() < doingList.get(i + 1).getPlanningDueDate() ?
                            doingList.get(i).getPlanningDueDate() : doingList.get(i + 1).getPlanningDueDate();

            BusyTime busyTime = new BusyTime(projectID, rangeStart, rangeEnd);
            busyTime.addRelatedTask(doingList.get(i).getTaskID());
            busyTime.addRelatedTask(doingList.get(i+1).getTaskID());
            busyInDoing.add(busyTime);
        }

        // done

        List<Task> doneList = doneTasks.getValue();
        for (int i = 0; i < doneList.size() - 1; i++) {
            Range<Long> actualRange = Range.between(doneList.get(i).getActualStartDate(), doneList.get(i).getActualDueDate());
            Range<Long> nextActualRange = Range.between(doneList.get(i + 1).getActualStartDate(), doneList.get(i + 1).getActualDueDate());
            if (!actualRange.isOverlappedBy(nextActualRange)) {
                break;
            }
            // Done list has been sorted by actual start date before
            // So i.getActualStartDate < (i+1).getActualStartDate
            long rangeStart = doneList.get(i + 1).getActualStartDate();
            long rangeEnd =
                    doneList.get(i).getActualDueDate() < doneList.get(i + 1).getActualDueDate() ?
                            doneList.get(i).getActualDueDate() : doneList.get(i + 1).getActualDueDate();

            BusyTime busyTime = new BusyTime(projectID, rangeStart, rangeEnd);
            busyTime.addRelatedTask(doneList.get(i).getTaskID());
            busyTime.addRelatedTask(doneList.get(i+1).getTaskID());
            busyInDone.add(busyTime);
//            Toast.makeText(application,"busyInDoing----------------------->"+busyInDoing.size(),Toast.LENGTH_SHORT).show();
        }

    }
    public List<BusyTime> getBusyInTodo(){
        return busyInTodo;
    }
    public List<BusyTime> getBusyInDoing() {
        return busyInDoing;
    }

    public List<BusyTime> getBusyInDone() {
        return busyInDone;
    }
}
