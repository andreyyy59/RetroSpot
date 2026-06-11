package com.retrowave.player.data.download;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class SpotifySearchRepository_Factory implements Factory<SpotifySearchRepository> {
  private final Provider<SpotifySearchApi> searchApiProvider;

  private final Provider<OkHttpClient> clientProvider;

  public SpotifySearchRepository_Factory(Provider<SpotifySearchApi> searchApiProvider,
      Provider<OkHttpClient> clientProvider) {
    this.searchApiProvider = searchApiProvider;
    this.clientProvider = clientProvider;
  }

  @Override
  public SpotifySearchRepository get() {
    return newInstance(searchApiProvider.get(), clientProvider.get());
  }

  public static SpotifySearchRepository_Factory create(Provider<SpotifySearchApi> searchApiProvider,
      Provider<OkHttpClient> clientProvider) {
    return new SpotifySearchRepository_Factory(searchApiProvider, clientProvider);
  }

  public static SpotifySearchRepository newInstance(SpotifySearchApi searchApi,
      OkHttpClient client) {
    return new SpotifySearchRepository(searchApi, client);
  }
}
