package com.retrowave.player.di

import com.retrowave.player.data.download.SpotifySearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifySearchModule {

    private const val SPOTIFY_API_BASE = "https://api.spotify.com/v1/"

    @Provides
    @Singleton
    @Named("spotify_search")
    fun provideSpotifySearchRetrofit(
        @Named("file_download") client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SPOTIFY_API_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifySearchApi(
        @Named("spotify_search") retrofit: Retrofit
    ): SpotifySearchApi {
        return retrofit.create(SpotifySearchApi::class.java)
    }
}
