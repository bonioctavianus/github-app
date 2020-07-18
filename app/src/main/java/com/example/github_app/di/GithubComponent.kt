package com.example.github_app.di

import com.example.github_app.GithubApp
import com.example.github_app.di.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class
    ]
)
interface GithubComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: GithubApp): GithubComponent
    }

    fun inject(application: GithubApp)
}