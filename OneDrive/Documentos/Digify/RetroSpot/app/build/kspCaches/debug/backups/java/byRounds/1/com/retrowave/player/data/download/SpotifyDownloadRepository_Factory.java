package com.retrowave.player.data.download;

import android.content.Context;
import com.retrowave.player.data.repository.MusicRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata({
    "javax.inject.Named",
    "dagger.hilt.android.qualifiers.ApplicationContext"
})
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SpotifyDownloadRepository_Factory implements Factory<SpotifyDownloadRepository> {
  private final Provider<SpotifyDownloadApi> apiProvider;

  private final Provider<MusicRepository> musicRepositoryProvider;

  private final Provider<OkHttpClient> downloadClientProvider;

  private final Provider<Context> contextProvider;

  public SpotifyDownloadRepository_Factory(Provider<SpotifyDownloadApi> apiProvider,
      Provider<MusicRepository> musicRepositoryProvider,
      Provider<OkHttpClient> downloadClientProvider, Provider<Context> contextProvider) {
    this.apiProvider = apiProvider;
    this.musicRepositoryProvider = musicRepositoryProvider;
    this.downloadClientProvider = downloadClientProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SpotifyDownloadRepository get() {
    return newInstance(apiProvider.get(), musicRepositoryProvider.get(), downloadClientProvider.get(), contextProvider.get());
  }

  public static SpotifyDownloadRepository_Factory create(Provider<SpotifyDownloadApi> apiProvider,
      Provider<MusicRepository> musicRepositoryProvider,
      Provider<OkHttpClient> downloadClientProvider, Provider<Context> contextProvider) {
    return new SpotifyDownloadRepository_Factory(apiProvider, musicRepositoryProvider, downloadClientProvider, contextProvider);
  }

  public static SpotifyDownloadRepository newInstance(SpotifyDownloadApi api,
      MusicRepository musicRepository, OkHttpClient downloadClient, Context context) {
    return new SpotifyDownloadRepository(api, musicRepository, downloadClient, context);
  }
}
