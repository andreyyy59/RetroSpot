package com.retrowave.player.ui.search;

import com.retrowave.player.data.download.SpotifyDownloadRepository;
import com.retrowave.player.data.download.SpotifySearchRepository;
import com.retrowave.player.data.repository.MusicRepository;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<MusicRepository> musicRepositoryProvider;

  private final Provider<SpotifyDownloadRepository> spotifyDownloadRepositoryProvider;

  private final Provider<SpotifySearchRepository> spotifySearchRepositoryProvider;

  public SearchViewModel_Factory(Provider<MusicRepository> musicRepositoryProvider,
      Provider<SpotifyDownloadRepository> spotifyDownloadRepositoryProvider,
      Provider<SpotifySearchRepository> spotifySearchRepositoryProvider) {
    this.musicRepositoryProvider = musicRepositoryProvider;
    this.spotifyDownloadRepositoryProvider = spotifyDownloadRepositoryProvider;
    this.spotifySearchRepositoryProvider = spotifySearchRepositoryProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(musicRepositoryProvider.get(), spotifyDownloadRepositoryProvider.get(), spotifySearchRepositoryProvider.get());
  }

  public static SearchViewModel_Factory create(Provider<MusicRepository> musicRepositoryProvider,
      Provider<SpotifyDownloadRepository> spotifyDownloadRepositoryProvider,
      Provider<SpotifySearchRepository> spotifySearchRepositoryProvider) {
    return new SearchViewModel_Factory(musicRepositoryProvider, spotifyDownloadRepositoryProvider, spotifySearchRepositoryProvider);
  }

  public static SearchViewModel newInstance(MusicRepository musicRepository,
      SpotifyDownloadRepository spotifyDownloadRepository,
      SpotifySearchRepository spotifySearchRepository) {
    return new SearchViewModel(musicRepository, spotifyDownloadRepository, spotifySearchRepository);
  }
}
