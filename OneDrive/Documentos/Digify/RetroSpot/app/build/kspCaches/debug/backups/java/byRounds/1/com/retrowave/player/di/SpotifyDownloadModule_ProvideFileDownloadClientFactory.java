package com.retrowave.player.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class SpotifyDownloadModule_ProvideFileDownloadClientFactory implements Factory<OkHttpClient> {
  @Override
  public OkHttpClient get() {
    return provideFileDownloadClient();
  }

  public static SpotifyDownloadModule_ProvideFileDownloadClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OkHttpClient provideFileDownloadClient() {
    return Preconditions.checkNotNullFromProvides(SpotifyDownloadModule.INSTANCE.provideFileDownloadClient());
  }

  private static final class InstanceHolder {
    private static final SpotifyDownloadModule_ProvideFileDownloadClientFactory INSTANCE = new SpotifyDownloadModule_ProvideFileDownloadClientFactory();
  }
}
