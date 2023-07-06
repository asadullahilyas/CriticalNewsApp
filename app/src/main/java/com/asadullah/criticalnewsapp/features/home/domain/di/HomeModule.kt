package com.asadullah.criticalnewsapp.features.home.domain.di

import com.asadullah.criticalnewsapp.api.ApiImpl
import com.asadullah.criticalnewsapp.features.home.data.repo.HomeRepoImpl
import com.asadullah.criticalnewsapp.features.home.domain.repo.HomeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    @ViewModelScoped
    fun getHomeRepo(apiImpl: ApiImpl): HomeRepo {
        return HomeRepoImpl(apiImpl)
    }
}