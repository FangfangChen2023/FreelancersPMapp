package com.chen.freelancerspmapp.Repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.RoomDatabase;

import com.chen.freelancerspmapp.AppDatabase;
import com.chen.freelancerspmapp.Dao.ProjectDao;
import com.chen.freelancerspmapp.Entity.Project;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class ProjectRepositoryImpl implements ProjectRepository {
    private AppDatabase db;
    private ExecutorService databaseWriteExecutor;
    private List<Project> allProjects;
//    Semaphore semaphore = new Semaphore(1, true);
//    CountDownLatch countDownLatch = new CountDownLatch(1);

    public ProjectRepositoryImpl(Application application) {
        db = AppDatabase.getDatabase(application);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Future insertProject(Project project) {
        ProjectDao projectDao = db.projectDao();

           Future future =  databaseWriteExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    //                        semaphore.acquire(1);
                    projectDao.insertProject(project);
                    Log.d("insert new project"+"\n", project.getName());
//                        semaphore.release();

                }
            });
           return future;
//        if (semaphore.availablePermits()<1){
//            try {
//                semaphore.wait();
//            }catch (Exception e){
//                throw new RuntimeException(e);
//            }
//        }

//        databaseWriteExecutor.shutdown();
    }

    @Override
    public void deleteProject(Project project) {
        ProjectDao projectDao = db.projectDao();
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                projectDao.deleteProject(project);
            }
        });
//        databaseWriteExecutor.shutdown();
    }

    @Override
    public Future queryAllProjects(RepositoryCallback callback) {
        ProjectDao projectDao = db.projectDao();
//        Using Future to make threads run in order
        Future future = databaseWriteExecutor.submit(new Runnable() {
            @Override
            public void run() {
                //                    semaphore.acquire(1);
                allProjects = projectDao.queryAllProjects();
                callback.onComplete(new MutableLiveData(allProjects));
//                    countDownLatch.countDown();
//                    semaphore.release();

            }
        });
        return future;
//        databaseWriteExecutor.shutdown();
//        try {
//            countDownLatch.await();
//          if (semaphore.availablePermits()<1){
//              semaphore.wait();
//          }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    @Override
    public void queryProject(Long projectID, RepositoryCallback callback) {
        ProjectDao projectDao = db.projectDao();
        databaseWriteExecutor.submit(new Runnable() {
            @Override
            public void run() {
                  Project project = projectDao.queryProject(projectID);
                callback.onComplete(new MutableLiveData(project));
//                semaphore.release();
            }
        });
//        databaseWriteExecutor.shutdown();
    }

}
