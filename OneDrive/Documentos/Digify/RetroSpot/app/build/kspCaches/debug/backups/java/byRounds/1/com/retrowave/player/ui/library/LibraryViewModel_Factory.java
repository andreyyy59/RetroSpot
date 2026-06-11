package com.retrowave.player.ui.library;

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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<MusicRepository> musicRepositoryProvider;

  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  public LibraryViewModel_Factory(Provider<MusicRepository> musicRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider) {
    this.musicRepositoryProvider = musicRepositoryProvider;
    this.playlistRepositoryProvider = playlistRepositoryProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(musicRepositoryProvider.get(), playlistRepositoryProvider.get());
  }

  public static LibraryViewModel_Factory create(Provider<MusicRepository> musicRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider) {
    return new LibraryViewModel_Factory(musicRepositoryProvider, playlistRepositoryProvider);
  }

  public static LibraryViewModel newInstance(MusicRepository musicRepository,
      PlaylistRepository playlistRepository) {
    return new LibraryViewModel(musicRepository, playlistRepository);
  }
}
