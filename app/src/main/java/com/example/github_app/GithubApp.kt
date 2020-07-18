package com.example.github_app

import android.app.Application
import com.example.github_app.di.DaggerGithubComponent
import com.example.github_app.di.GithubComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

class GithubApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var mAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var mComponent: GithubComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initDagger()

        RxJavaPlugins.setErrorHandler { exception ->
            Timber.e("RxJava unhandled exception: $exception")
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDagger() {
        mComponent = DaggerGithubComponent.factory().create(this)
        mComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = mAndroidInjector
}