package com.chen.freelancerspmapp.Repository;

import android.app.Application;
import android.service.carrier.CarrierMessagingService;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chen.freelancerspmapp.AppDatabase;
import com.chen.freelancerspmapp.AppExecutors;
import com.chen.freelancerspmapp.Dao.TaskDao;
import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class TaskRepositoryImpl implements TaskRepository {
    private AppDatabase db;
    private ExecutorService databaseWriteExecutor;
    private List<Task> allTasks;
//    Semaphore semaphore = new Semaphore(1);
//    CountDownLatch countDownLatch = new CountDownLatch(1);
AppExecutors executors;

    public TaskRepositoryImpl(Application application) {
        db = AppDatabase.getDatabase(application);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
//        executors = AppExecutors.getInstance();
    }

    @Override
    public Future insertTask(Task task) {
        TaskDao taskDao = db.taskDao();
        Future future = databaseWriteExecutor.submit(() ->
        {
            taskDao.insertTask(task);
        });

        return future;
    }

    @Override
    public Future deleteTask(Task task) {
        TaskDao taskDao = db.taskDao();
        Future future = databaseWriteExecutor.submit(() ->
        {
            taskDao.deleteTask(task);
        });

        return future;
    }


    @Override
    public Future queryAllTasks(Long projectID, RepositoryCallback callback) {
        TaskDao taskDao = db.taskDao();
        Future future = databaseWriteExecutor.submit(() ->
        {
             allTasks = taskDao.queryAllTasks(projectID);
            callback.onComplete(new MutableLiveData(allTasks));
//            countDownLatch.countDown();
//            semaphore.release();
        });
        //            countDownLatch.await();
//            semaphore.acquire(1);
//        databaseWriteExecutor.shutdown();
        return future;
    }

    @Override
    public void queryTask(Long taskID, Long projectID, RepositoryCallback callback) {
        TaskDao taskDao = db.taskDao();
        databaseWriteExecutor.submit(() ->
        {
            Task task = taskDao.queryTask(taskID, projectID);
            callback.onComplete(new MutableLiveData(task));
//            semaphore.release();
        });
        //            semaphore.acquire();
//        databaseWriteExecutor.shutdown();
    }

    @Override
    public Future updateTask(Task task) {
        TaskDao taskDao = db.taskDao();
        Future future = databaseWriteExecutor.submit(() ->
        {
            taskDao.insertTask(task);
        });
        return future;
    }
}
