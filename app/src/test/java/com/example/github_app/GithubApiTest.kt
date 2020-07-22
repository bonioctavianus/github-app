package com.example.github_app

import com.example.github_app.repository.GithubApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GithubApiTest {

    private val mWebServer = MockWebServer()
    private lateinit var mApi: GithubApi

    @Before
    fun setup() {
        mWebServer.start()

        mApi = Retrofit.Builder()
            .baseUrl(mWebServer.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }

    @After
    fun teardown() {
        mWebServer.shutdown()
    }

    @Test
    fun `search user - should return list of users`() {
        val query = "bonioctavianus"
        val perPage = 20
        val page = 1

        val mockBody = TestUtilities.getResponseBodyFromJsonFile(
            filename = "response_search_user_200.json"
        )

        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(mockBody)

        mWebServer.enqueue(mockResponse)

        val response = mApi.searchUsers(query, perPage, page).blockingGet()
        val request = mWebServer.takeRequest()

        assert(request.method == "GET")
        assert(request.path == "/search/users?q=bonioctavianus&per_page=20&page=1")

        assert(response.users!!.first().id == 13826685)
        assert(response.users!!.first().username == "bonioctavianus")
        assert(response.users!!.first().avatar == "https://avatars0.githubusercontent.com/u/13826685?v=4")
    }
}