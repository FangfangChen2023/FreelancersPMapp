package com.chen.freelancerspmapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Long projectID;
    private Application application;

    public MyViewModelFactory(Application application) {
        this.application = application;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(application, projectID);
        }
        if (modelClass.isAssignableFrom(ProjectViewModel.class)) {
            return (T) new ProjectViewModel(application);
        }
        if (modelClass.isAssignableFrom(SharedViewModel.class)) {
            return (T) new SharedViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
