package com.example.github_app.usecase

import com.example.github_app.network.SchedulersPool
import com.example.github_app.repository.GithubRepository
import io.reactivex.Observable
import javax.inject.Inject

class SearchUser @Inject constructor(
    private val mRepository: GithubRepository,
    private val mSchedulers: SchedulersPool
) {

    fun searchUsers(query: String, perPage: Int, page: Int): Observable<Result> {
        return mRepository.searchUsers(query, perPage, page)
            .toObservable()
            .subscribeOn(mSchedulers.io())
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .startWith(Result.InFlight)
            .onErrorReturn { Result.Error(it) }
            .observeOn(mSchedulers.ui())
    }
}