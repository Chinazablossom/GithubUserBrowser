package com.example.githubuserbrowser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuserbrowser.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getAllUsersOnce(): List<UserEntity>

    @Query("SELECT * FROM users WHERE id > :since ORDER BY id ASC LIMIT :limit")
    suspend fun getUsersSince(since: Long, limit: Int): List<UserEntity>

    @Query("SELECT MAX(id) FROM users")
    suspend fun getMaxUserId(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}
