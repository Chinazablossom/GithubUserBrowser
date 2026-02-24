package com.example.githubuserbrowser.di

import android.app.Application
import androidx.room.Room
import com.example.githubuserbrowser.data.local.GithubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGithubDatabase(
        application: Application
    ): GithubDatabase {
        return Room.databaseBuilder(
            application,
            GithubDatabase::class.java,
            GithubDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(database: GithubDatabase) = database.userDao()

    @Provides
    fun provideUserDetailDao(database: GithubDatabase) = database.userDetailDao()
}
