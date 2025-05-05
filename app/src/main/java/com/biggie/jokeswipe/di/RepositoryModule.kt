package com.biggie.jokeswipe.di

import com.biggie.jokeswipe.data.repository.JokeRepositoryImpl
import com.biggie.jokeswipe.domain.repository.JokeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJokeRepository(
        impl: JokeRepositoryImpl
    ): JokeRepository
}