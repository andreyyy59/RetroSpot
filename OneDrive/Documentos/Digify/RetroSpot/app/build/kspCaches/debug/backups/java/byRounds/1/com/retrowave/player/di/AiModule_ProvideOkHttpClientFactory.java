package com.retrowave.player.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class AiModule_ProvideOkHttpClientFactory implements Factory<OkHttpClient> {
  @Override
  public OkHttpClient get() {
    return provideOkHttpClient();
  }

  public static AiModule_ProvideOkHttpClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OkHttpClient provideOkHttpClient() {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideOkHttpClient());
  }

  private static final class InstanceHolder {
    private static final AiModule_ProvideOkHttpClientFactory INSTANCE = new AiModule_ProvideOkHttpClientFactory();
  }
}
