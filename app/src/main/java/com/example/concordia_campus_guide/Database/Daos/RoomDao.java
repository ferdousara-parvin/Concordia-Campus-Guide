package com.example.concordia_campus_guide.Database.Daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.concordia_campus_guide.Models.RoomModel;

import java.util.List;

@Dao
public interface RoomDao {

//    Fix the query here later
    @Query("SELECT *  FROM rooms")
    List<RoomModel> getAllRoomsForFloorInBuiling();


    @Insert
    void insertAll(List<RoomModel> rooms);


    @Delete
    void deleteAll(RoomModel... rooms);


}