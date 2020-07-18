package com.example.github_app.di.network


import com.example.github_app.BuildConfig
import com.example.github_app.GithubApp
import com.example.github_app.network.HttpRequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return BuildConfig.HOST
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpCache(application: GithubApp): Cache {
        val size = (5 * 1024 * 1024).toLong() // 5Mb‬
        return Cache(application.cacheDir, size)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: HttpRequestInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(requestInterceptor)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}