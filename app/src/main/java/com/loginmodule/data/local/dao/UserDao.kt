package com.loginmodule.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.loginmodule.data.local.table.UserEntity

/**
 * Dao for saving location in room database
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM tb_user WHERE email=:email")
    fun loadUserByEmailId(email: String): LiveData<UserEntity>


    @Query("SELECT * FROM tb_user WHERE user_id=:userId")
    fun loadUserByUserId(userId: String): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userEntities: UserEntity)


    @Query("UPDATE tb_user SET avatar_url=:avatar WHERE user_id = :userId")
    suspend fun updateUserImage(userId: String, avatar: String);

    @Transaction
    @Query("DELETE FROM tb_user")
    suspend fun deleteUser()
}
