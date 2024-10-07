package com.example.krishna.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ServiceEntity::class], version = 1)
abstract class DataBase :RoomDatabase() {

    abstract fun userDao(): ServiceDao

    companion object {
        @Volatile private var instance: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "app_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }


}