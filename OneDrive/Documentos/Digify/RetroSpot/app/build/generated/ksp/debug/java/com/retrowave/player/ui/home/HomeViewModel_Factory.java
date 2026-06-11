package com.retrowave.player.ui.home;

import com.retrowave.player.data.ai.RecommendationEngine;
import com.retrowave.player.data.repository.MusicRepository;
import com.retrowave.player.data.repository.PlaylistRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<MusicRepository> musicRepositoryProvider;

  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  private final Provider<RecommendationEngine> recommendationEngineProvider;

  public HomeViewModel_Factory(Provider<MusicRepository> musicRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<RecommendationEngine> recommendationEngineProvider) {
    this.musicRepositoryProvider = musicRepositoryProvider;
    this.playlistRepositoryProvider = playlistRepositoryProvider;
    this.recommendationEngineProvider = recommendationEngineProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(musicRepositoryProvider.get(), playlistRepositoryProvider.get(), recommendationEngineProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<MusicRepository> musicRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<RecommendationEngine> recommendationEngineProvider) {
    return new HomeViewModel_Factory(musicRepositoryProvider, playlistRepositoryProvider, recommendationEngineProvider);
  }

  public static HomeViewModel newInstance(MusicRepository musicRepository,
      PlaylistRepository playlistRepository, RecommendationEngine recommendationEngine) {
    return new HomeViewModel(musicRepository, playlistRepository, recommendationEngine);
  }
}
