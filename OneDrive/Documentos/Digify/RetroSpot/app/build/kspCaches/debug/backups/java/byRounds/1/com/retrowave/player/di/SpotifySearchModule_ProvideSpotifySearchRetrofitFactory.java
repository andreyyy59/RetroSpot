package com.retrowave.player.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class SpotifySearchModule_ProvideSpotifySearchRetrofitFactory implements Factory<Retrofit> {
  private final Provider<OkHttpClient> clientProvider;

  public SpotifySearchModule_ProvideSpotifySearchRetrofitFactory(
      Provider<OkHttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public Retrofit get() {
    return provideSpotifySearchRetrofit(clientProvider.get());
  }

  public static SpotifySearchModule_ProvideSpotifySearchRetrofitFactory create(
      Provider<OkHttpClient> clientProvider) {
    return new SpotifySearchModule_ProvideSpotifySearchRetrofitFactory(clientProvider);
  }

  public static Retrofit provideSpotifySearchRetrofit(OkHttpClient client) {
    return Preconditions.checkNotNullFromProvides(SpotifySearchModule.INSTANCE.provideSpotifySearchRetrofit(client));
  }
}
