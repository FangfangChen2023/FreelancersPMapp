package com.chen.freelancerspmapp.viewmodel;

import android.app.Application;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Repository.ProjectRepositoryImpl;
import com.chen.freelancerspmapp.Repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


public class ProjectViewModel extends AndroidViewModel {

    private ProjectRepositoryImpl projectRepository;
    private LiveData<List<Project>> allProjects;
    private MutableLiveData<List<Project>> currentProj;
    private MutableLiveData<List<Project>> historyProj;


    public ProjectViewModel(@NonNull Application application) {
        super(application);
        projectRepository = new ProjectRepositoryImpl(application);
        try{
            getAllProjectsFromDB().get();
        }catch (Exception e){
            throw new RuntimeException();
        }

        currentProj = new MutableLiveData<>();
        historyProj = new MutableLiveData<>();

        // TODO : test data
        if(allProjects == null || allProjects.getValue() == null || allProjects.getValue().size() == 0){

        }else {

            categorizeProjects();
        }
    }

    public LiveData<List<Project>> getCurrentProjects() {
        return currentProj;
    }
    public List<Project> getHistoryProjects() {
        return historyProj.getValue();
    }

    public void categorizeProjects(){
        List<Project> tempCurrent = new ArrayList<>();
        List<Project> tempHistory = new ArrayList<>();

        if(allProjects.getValue() == null){
            try{
                Thread.sleep(3000);
            }catch (Exception e){
                Log.d("main thread sleep exception===","+++++++++");
            }

        }else {
            allProjects.getValue().forEach((project) -> {
                if (project.getStatus() == 0) {
                    tempCurrent.add(project);
                }
                if (project.getStatus() == 1) {
                    tempHistory.add(project);
                }
            });
            currentProj.setValue(tempCurrent);
            historyProj.setValue(tempHistory);
        }

    }

    public LiveData<List<Project>> getAllProjects(){
        return allProjects;
    }

    public Future getAllProjectsFromDB() {
        // Using Future to make threads run in order
        Future future = projectRepository.queryAllProjects(queryResult -> {
            allProjects = queryResult;
//                allProjects.getValue().forEach((project -> Log.d(project.getProjectID()+">>>>>", project.getName())));
        });
        return future;
    }

    public LiveData<Project> getProject(Long projectID) {
        final LiveData<Project>[] project = new LiveData[]{new MutableLiveData<>()};
        projectRepository.queryProject(projectID, new RepositoryCallback() {
            @Override
            public void onComplete(LiveData queryResult) {
                project[0] = queryResult;
            }
        });
        return project[0];
    }

    public void insertProject(Project project) {
        try {
         //   Using Future to make threads run in order. Making sure that categorizeProjects() waits until insert and getAll are both done.
            projectRepository.insertProject(project).get();
            getAllProjectsFromDB().get();   // Read the database again.
        }catch (Exception e){
            throw new RuntimeException();
        }
        categorizeProjects();
    }

    public void updateProject(Project project) {
        insertProject(project);
    }

    public void deleteProject(Project project) {
        projectRepository.deleteProject(project);
        getAllProjectsFromDB();
        categorizeProjects();
    }

}
