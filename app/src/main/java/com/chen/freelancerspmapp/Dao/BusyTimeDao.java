package com.chen.freelancerspmapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.chen.freelancerspmapp.Entity.BusyTime;
import java.util.List;

@Dao
public interface BusyTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBusyTime(BusyTime task);
    @Delete
    void deleteBusyTime(BusyTime task);

    @Query("SELECT * FROM busytime_table WHERE projectID=:projectID")
    List<BusyTime> queryAllBusyTime(Long projectID);
}
