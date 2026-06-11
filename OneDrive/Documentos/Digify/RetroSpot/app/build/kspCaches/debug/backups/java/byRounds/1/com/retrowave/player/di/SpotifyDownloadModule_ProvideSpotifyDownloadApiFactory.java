package com.retrowave.player.di;

import com.retrowave.player.data.download.SpotifyDownloadApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class SpotifyDownloadModule_ProvideSpotifyDownloadApiFactory implements Factory<SpotifyDownloadApi> {
  @Override
  public SpotifyDownloadApi get() {
    return provideSpotifyDownloadApi();
  }

  public static SpotifyDownloadModule_ProvideSpotifyDownloadApiFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SpotifyDownloadApi provideSpotifyDownloadApi() {
    return Preconditions.checkNotNullFromProvides(SpotifyDownloadModule.INSTANCE.provideSpotifyDownloadApi());
  }

  private static final class InstanceHolder {
    private static final SpotifyDownloadModule_ProvideSpotifyDownloadApiFactory INSTANCE = new SpotifyDownloadModule_ProvideSpotifyDownloadApiFactory();
  }
}
