package com.chen.freelancerspmapp.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.chen.freelancerspmapp.Entity.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProject(Project project);
    @Delete
    void deleteProject(Project project);

//    @Query("SELECT * FROM project_table")
//    LiveData<List<Project>> queryAllProjects();
    @Query("SELECT * FROM project_table")
    List<Project> queryAllProjects();

    @Query("SELECT * FROM project_table WHERE projectID = :projectID")
    Project queryProject(Long projectID);
}
