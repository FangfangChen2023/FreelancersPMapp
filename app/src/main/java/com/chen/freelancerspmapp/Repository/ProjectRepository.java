package com.chen.freelancerspmapp.Repository;

import androidx.lifecycle.LiveData;
import com.chen.freelancerspmapp.Entity.Project;
import java.util.List;
import java.util.concurrent.Future;

public interface ProjectRepository {

    Future insertProject(Project project);
    void deleteProject(Project project);

    Future queryAllProjects(RepositoryCallback callback);

    void queryProject(Long projectID, RepositoryCallback callback);
}
