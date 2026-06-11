package com.retrowave.player.di;

import com.retrowave.player.data.ai.GeminiApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class AiModule_ProvideGeminiApiFactory implements Factory<GeminiApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AiModule_ProvideGeminiApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public GeminiApi get() {
    return provideGeminiApi(retrofitProvider.get());
  }

  public static AiModule_ProvideGeminiApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AiModule_ProvideGeminiApiFactory(retrofitProvider);
  }

  public static GeminiApi provideGeminiApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideGeminiApi(retrofit));
  }
}
