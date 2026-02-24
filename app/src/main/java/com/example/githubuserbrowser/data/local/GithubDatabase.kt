package com.example.githubuserbrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubuserbrowser.data.local.entity.UserDetailEntity
import com.example.githubuserbrowser.data.local.entity.UserEntity
@Database(
    entities = [UserEntity::class, UserDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userDetailDao(): UserDetailDao

    companion object {
        const val DATABASE_NAME = "github_database"
    }
}
