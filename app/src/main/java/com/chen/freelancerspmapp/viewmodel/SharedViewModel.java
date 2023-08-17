package com.chen.freelancerspmapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Repository.ProjectRepository;
import com.chen.freelancerspmapp.Repository.ProjectRepositoryImpl;
import com.chen.freelancerspmapp.Repository.RepositoryCallback;
import com.chen.freelancerspmapp.Repository.TaskRepository;
import com.chen.freelancerspmapp.Repository.TaskRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    private final static MutableLiveData<Project> currentProj = new MutableLiveData<>();
    private LiveData<List<Project>> projectList = new MutableLiveData<>();
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;


    public SharedViewModel(@NonNull Application application) {
        super(application);

        projectRepository = new ProjectRepositoryImpl(application);

        try {
            projectRepository.queryAllProjects(queryResult -> {
                projectList = queryResult;
            }).get();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        taskRepository = new TaskRepositoryImpl(application);
        try {

        }catch (Exception e){
            throw new RuntimeException();
        }

        if (projectList.getValue() != null && projectList.getValue().size() > 0) {
            currentProj.setValue(projectList.getValue().get(projectList.getValue().size() - 1));
            Log.d("SharedViewModel------------------------currentProj:", currentProj.getValue().getName());
        }
    }

    public MutableLiveData<Project> getCurrentProj() {
        return currentProj;
    }

    public void setCurrentProj(Project currentProj) {
        this.currentProj.setValue(currentProj);
    }

//    public

}
