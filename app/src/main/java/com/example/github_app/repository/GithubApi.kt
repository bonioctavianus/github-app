package com.example.github_app.repository

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET(value = "search/users")
    fun searchUsers(
        @Query(value = "q") query: String,
        @Query(value = "per_page") perPage: Int,
        @Query(value = "page") page: Int
    ): Single<SearchUserResponse>
}