package com.chen.freelancerspmapp.Repository;

import android.service.carrier.CarrierMessagingService;

import androidx.lifecycle.LiveData;
import com.chen.freelancerspmapp.Entity.Task;

import java.util.List;
import java.util.concurrent.Future;

public interface TaskRepository {
    Future insertTask(Task task);
    Future deleteTask(Task task);

    Future queryAllTasks(Long projectID, RepositoryCallback callback);

    void queryTask(Long taskID, Long projectID, RepositoryCallback callback);

    Future updateTask(Task task);
}
