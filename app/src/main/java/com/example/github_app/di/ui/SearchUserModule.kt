package com.example.github_app.di.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.github_app.ui.SearchUserFragment
import com.example.github_app.ui.SearchUserViewModel
import com.example.github_app.ui.SearchUserViewModelFactory
import dagger.Module
import dagger.Provides

@Module
object SearchUserModule {

    @Provides
    fun provideViewModelStoreOwner(fragment: SearchUserFragment): ViewModelStoreOwner {
        return fragment
    }

    @Provides
    fun provideSearchUserViewModel(
        owner: ViewModelStoreOwner,
        factory: SearchUserViewModelFactory
    ): SearchUserViewModel {
        return ViewModelProvider(owner, factory).get(SearchUserViewModel::class.java)
    }
}