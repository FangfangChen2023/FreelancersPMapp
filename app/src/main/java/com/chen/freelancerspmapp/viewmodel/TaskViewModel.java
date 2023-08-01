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

public class TaskViewModel extends AndroidViewModel {

    private TaskRepositoryImpl taskRepository;
    private LiveData<List<Task>> allTasks;
    private MutableLiveData<List<Task>> todoTasks = new MutableLiveData<>();
    private MutableLiveData<List<Task>> doingTasks = new MutableLiveData<>();
    private MutableLiveData<List<Task>> doneTasks = new MutableLiveData<>();

    public TaskViewModel(@NonNull Application application, Long projectID) {
        super(application);
        taskRepository = new TaskRepositoryImpl(application);
        initializeLists(projectID);

    }

    public void initializeLists(Long projectID) {
        Log.d("initializeLists", "@@@@@@@@@@@@@@@");
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
        return allTasks;
    }

    public void categorizeTasks() {
        List<Task> todoTemp = new ArrayList<>();
        List<Task> doingTemp = new ArrayList<>();
        List<Task> doneTemp = new ArrayList<>();
        if (allTasks != null && allTasks.getValue() != null && allTasks.getValue().size() > 0) {
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

        todoTasks.setValue(todoTemp);
        doingTasks.setValue(doingTemp);
        doneTasks.setValue(doneTemp);
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
