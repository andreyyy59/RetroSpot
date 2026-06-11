package com.retrowave.player.di

import com.retrowave.player.BuildConfig
import com.retrowave.player.data.download.SpotifyDownloadApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifyDownloadModule {

    private const val SPOTIFY_DOWNLOAD_BASE_URL = "https://spotify-downloader9.p.rapidapi.com/"

    @Provides
    @Singleton
    fun provideSpotifyDownloadApi(): SpotifyDownloadApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-host", "spotify-downloader9.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", BuildConfig.RAPIDAPI_KEY)
                    .build()
                chain.proceed(request)
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SPOTIFY_DOWNLOAD_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SpotifyDownloadApi::class.java)
    }

    @Provides
    @Singleton
    @Named("file_download")
    fun provideFileDownloadClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()
    }
}
