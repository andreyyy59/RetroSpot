package com.retrowave.player.data.ai;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class RecommendationEngine_Factory implements Factory<RecommendationEngine> {
  private final Provider<GeminiApi> geminiApiProvider;

  public RecommendationEngine_Factory(Provider<GeminiApi> geminiApiProvider) {
    this.geminiApiProvider = geminiApiProvider;
  }

  @Override
  public RecommendationEngine get() {
    return newInstance(geminiApiProvider.get());
  }

  public static RecommendationEngine_Factory create(Provider<GeminiApi> geminiApiProvider) {
    return new RecommendationEngine_Factory(geminiApiProvider);
  }

  public static RecommendationEngine newInstance(GeminiApi geminiApi) {
    return new RecommendationEngine(geminiApi);
  }
}
