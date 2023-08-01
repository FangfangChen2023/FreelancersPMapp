package com.chen.freelancerspmapp.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Entity.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);
    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM task_table WHERE projectID=:projectID")
    List<Task> queryAllTasks(Long projectID);
    @Query("SELECT * FROM task_table WHERE taskID=:taskID AND projectID=:projectID")
   Task queryTask(Long taskID, Long projectID);
}
