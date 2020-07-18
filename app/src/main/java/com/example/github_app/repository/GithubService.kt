package com.example.github_app.repository

import io.reactivex.Single

interface GithubService {
    fun searchUsers(query: String, perPage: Int, page: Int): Single<SearchUserResponse>
}

class GithubServiceV1(private val mApi: GithubApi) : GithubService {

    override fun searchUsers(query: String, perPage: Int, page: Int): Single<SearchUserResponse> {
        return mApi.searchUsers(query, perPage, page)
    }
}