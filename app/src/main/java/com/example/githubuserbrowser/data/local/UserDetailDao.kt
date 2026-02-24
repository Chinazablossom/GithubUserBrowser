package com.example.githubuserbrowser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuserbrowser.data.local.entity.UserDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailDao {

    @Query("SELECT * FROM user_details WHERE login = :username LIMIT 1")
    fun getUserDetail(username: String): Flow<UserDetailEntity?>

    @Query("SELECT * FROM user_details WHERE login = :username LIMIT 1")
    suspend fun getUserDetailOnce(username: String): UserDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userDetail: UserDetailEntity)

    @Query("DELETE FROM user_details WHERE login = :username")
    suspend fun deleteUserDetail(username: String)

    @Query("DELETE FROM user_details")
    suspend fun clearAll()
}
