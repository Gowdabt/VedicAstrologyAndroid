package com.astrologyvedic.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.astrologyvedic.app.data.api.AstrologyApi
import com.astrologyvedic.app.data.api.GeocodingApi
import com.astrologyvedic.app.data.local.AppDatabase
import com.astrologyvedic.app.data.local.dao.ChatDao
import com.astrologyvedic.app.data.local.dao.MantraCountDao
import com.astrologyvedic.app.data.local.dao.ProfileDao
import com.astrologyvedic.app.data.local.dao.ReportHistoryDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "vedic_astrology_prefs"
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setLenient()
            .create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://astrologyvedic.com/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideAstrologyApi(retrofit: Retrofit): AstrologyApi =
        retrofit.create(AstrologyApi::class.java)

    @Provides
    @Singleton
    fun provideGeocodingApi(
        @ApplicationContext context: Context,
        gson: Gson,
        loggingInterceptor: HttpLoggingInterceptor
    ): GeocodingApi {
        val geocodingOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "VedicAstrologyApp/1.0 (Android)")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val geocodingRetrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(geocodingOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return geocodingRetrofit.create(GeocodingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "vedic_astrology_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideProfileDao(database: AppDatabase): ProfileDao =
        database.profileDao()

    @Provides
    @Singleton
    fun provideChatDao(database: AppDatabase): ChatDao =
        database.chatDao()

    @Provides
    @Singleton
    fun provideReportHistoryDao(database: AppDatabase): ReportHistoryDao =
        database.reportHistoryDao()

    @Provides
    @Singleton
    fun provideMantraCountDao(database: AppDatabase): MantraCountDao =
        database.mantraCountDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}
