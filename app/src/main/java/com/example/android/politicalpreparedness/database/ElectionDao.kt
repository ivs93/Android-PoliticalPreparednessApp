package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table")
    fun getElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :key")
    fun getElectionWithId(key: Int): LiveData<Election>

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id = :key")
    fun deleteById(key: Int)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    fun clear()
}