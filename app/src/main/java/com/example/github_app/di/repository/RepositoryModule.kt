package com.example.github_app.di.repository

import com.example.github_app.repository.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object RepositoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGithubApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGithubService(api: GithubApi): GithubService {
        return GithubServiceV1(api)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGithubRepository(
        service: GithubService,
        transformer: GithubTransformer
    ): GithubRepository {
        return GithubRepositoryV1(service, transformer)
    }
}