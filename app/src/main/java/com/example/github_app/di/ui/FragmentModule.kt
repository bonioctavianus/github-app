package com.example.github_app.di.ui

import com.example.github_app.ui.search.SearchUserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [SearchUserModule::class])
    abstract fun contributesSearchUserFragment(): SearchUserFragment
}