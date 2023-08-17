package com.chen.freelancerspmapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.Repository.RepositoryCallback;
import com.chen.freelancerspmapp.Repository.TaskRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepositoryImpl taskRepository;
    private  LiveData<List<Task>> allTasks = new MutableLiveData<>();
    private static MutableLiveData<List<Task>> workflowTasks = new MutableLiveData<>();
    private MutableLiveData<List<Task>> todoTasks = new MutableLiveData<>();
     private static MutableLiveData<List<Task>> doingTasks = new MutableLiveData<>();
     private static MutableLiveData<List<Task>> doneTasks = new MutableLiveData<>();
     private CustomComparator taskComparator = new CustomComparator();

    public TaskViewModel(@NonNull Application application, Long projectID) {
        super(application);
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

}
