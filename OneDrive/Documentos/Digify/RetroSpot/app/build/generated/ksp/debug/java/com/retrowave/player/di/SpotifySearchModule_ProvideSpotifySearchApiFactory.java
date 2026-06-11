package com.retrowave.player.di;

import com.retrowave.player.data.download.SpotifySearchApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class SpotifySearchModule_ProvideSpotifySearchApiFactory implements Factory<SpotifySearchApi> {
  private final Provider<Retrofit> retrofitProvider;

  public SpotifySearchModule_ProvideSpotifySearchApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public SpotifySearchApi get() {
    return provideSpotifySearchApi(retrofitProvider.get());
  }

  public static SpotifySearchModule_ProvideSpotifySearchApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new SpotifySearchModule_ProvideSpotifySearchApiFactory(retrofitProvider);
  }

  public static SpotifySearchApi provideSpotifySearchApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(SpotifySearchModule.INSTANCE.provideSpotifySearchApi(retrofit));
  }
}
