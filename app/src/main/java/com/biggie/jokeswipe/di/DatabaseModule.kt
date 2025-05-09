package com.biggie.jokeswipe.di

import android.content.Context
import androidx.room.Room
import com.biggie.jokeswipe.data.db.JokeDao
import com.biggie.jokeswipe.data.db.JokeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext ctx: Context
    ): JokeDatabase {
        return Room.databaseBuilder(ctx, JokeDatabase::class.java, "joke_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideJokeDao(db: JokeDatabase): JokeDao = db.jokeDao()
}