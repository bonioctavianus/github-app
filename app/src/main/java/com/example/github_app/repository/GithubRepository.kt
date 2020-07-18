package com.example.github_app.repository

import com.example.github_app.domain.SearchUserResult
import io.reactivex.Single

interface GithubRepository {
    fun searchUsers(query: String, perPage: Int, page: Int): Single<SearchUserResult>
}

class GithubRepositoryV1(
    private val mService: GithubService,
    private val mTransformer: GithubTransformer
) : GithubRepository {

    override fun searchUsers(query: String, perPage: Int, page: Int): Single<SearchUserResult> {
        return mService.searchUsers(query, perPage, page)
            .map { mTransformer.transform(it) }
    }
}