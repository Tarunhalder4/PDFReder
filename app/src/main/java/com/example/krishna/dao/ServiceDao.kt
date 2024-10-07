package com.example.krishna.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(service: ServiceEntity)

    @Query("SELECT * FROM service")
    fun getService(): List<ServiceEntity>

    @Query("UPDATE service SET quantity = :quantity WHERE id = :id")
    suspend fun update(quantity: Int,id:String)

    @Query("DELETE FROM service WHERE id = :id")
    suspend fun delete(id:String)
}