package com.retrowave.player.domain.usecase;

import com.retrowave.player.data.ai.RecommendationEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class GetRecommendationsUseCase_Factory implements Factory<GetRecommendationsUseCase> {
  private final Provider<RecommendationEngine> recommendationEngineProvider;

  public GetRecommendationsUseCase_Factory(
      Provider<RecommendationEngine> recommendationEngineProvider) {
    this.recommendationEngineProvider = recommendationEngineProvider;
  }

  @Override
  public GetRecommendationsUseCase get() {
    return newInstance(recommendationEngineProvider.get());
  }

  public static GetRecommendationsUseCase_Factory create(
      Provider<RecommendationEngine> recommendationEngineProvider) {
    return new GetRecommendationsUseCase_Factory(recommendationEngineProvider);
  }

  public static GetRecommendationsUseCase newInstance(RecommendationEngine recommendationEngine) {
    return new GetRecommendationsUseCase(recommendationEngine);
  }
}
