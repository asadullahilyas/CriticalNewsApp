package com.asadullah.criticalnewsapp.di

import android.content.Context
import com.asadullah.criticalnewsapp.BuildConfig
import com.asadullah.criticalnewsapp.api.ApiImpl
import com.asadullah.criticalnewsapp.api.ApiInterface
import com.asadullah.criticalnewsapp.common.Settings
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getMoshiObject(): Moshi {
        return Moshi
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
                }
            )
            .connectTimeout(13, TimeUnit.SECONDS)
            .readTimeout(13, TimeUnit.SECONDS)
            .writeTimeout(13, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun getBaseUrl(): String {
        return "https://newsapi.org/v2/"
    }

    @Provides
    @Singleton
    fun provideApiService(baseUrl: String, moshi: Moshi, okHttpClient: OkHttpClient): ApiInterface {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            )
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideApiImpl(apiInterface: ApiInterface): ApiImpl {
        return ApiImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext context: Context): Settings {
        return Settings(context)
    }
}