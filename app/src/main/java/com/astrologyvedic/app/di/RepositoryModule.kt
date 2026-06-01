package com.astrologyvedic.app.di

import com.astrologyvedic.app.data.api.AstrologyApi
import com.astrologyvedic.app.data.local.dao.ChatDao
import com.astrologyvedic.app.data.local.dao.MantraCountDao
import com.astrologyvedic.app.data.local.dao.ProfileDao
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ChatRepository
import com.astrologyvedic.app.data.repository.MantraRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAstrologyRepository(
        api: AstrologyApi,
        gson: Gson
    ): AstrologyRepository =
        AstrologyRepository(api, gson)

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileDao: ProfileDao
    ): ProfileRepository =
        ProfileRepository(profileDao)

    @Provides
    @Singleton
    fun provideChatRepository(
        chatDao: ChatDao,
        astrologyRepository: AstrologyRepository
    ): ChatRepository =
        ChatRepository(chatDao, astrologyRepository)

    @Provides
    @Singleton
    fun provideMantraRepository(
        mantraCountDao: MantraCountDao
    ): MantraRepository =
        MantraRepository(mantraCountDao)
}
