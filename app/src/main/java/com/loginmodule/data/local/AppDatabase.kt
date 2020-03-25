package com.loginmodule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

import com.loginmodule.data.local.dao.UserDao
import com.loginmodule.data.local.table.UserEntity

/**
 * Database defination for define tables and Dao
 */
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Providing Dao of location database
    abstract fun userDao(): UserDao
}
