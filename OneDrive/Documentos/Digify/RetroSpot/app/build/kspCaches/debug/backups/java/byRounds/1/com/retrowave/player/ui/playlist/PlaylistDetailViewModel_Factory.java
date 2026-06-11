package com.retrowave.player.ui.playlist;

import androidx.lifecycle.SavedStateHandle;
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
public final class PlaylistDetailViewModel_Factory implements Factory<PlaylistDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  private final Provider<MusicRepository> musicRepositoryProvider;

  public PlaylistDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<MusicRepository> musicRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.playlistRepositoryProvider = playlistRepositoryProvider;
    this.musicRepositoryProvider = musicRepositoryProvider;
  }

  @Override
  public PlaylistDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), playlistRepositoryProvider.get(), musicRepositoryProvider.get());
  }

  public static PlaylistDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<MusicRepository> musicRepositoryProvider) {
    return new PlaylistDetailViewModel_Factory(savedStateHandleProvider, playlistRepositoryProvider, musicRepositoryProvider);
  }

  public static PlaylistDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      PlaylistRepository playlistRepository, MusicRepository musicRepository) {
    return new PlaylistDetailViewModel(savedStateHandle, playlistRepository, musicRepository);
  }
}
