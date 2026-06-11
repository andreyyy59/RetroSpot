package com.retrowave.player.ui.playlist;

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
public final class AlbumDetailViewModel_Factory implements Factory<AlbumDetailViewModel> {
  private final Provider<MusicRepository> musicRepositoryProvider;

  public AlbumDetailViewModel_Factory(Provider<MusicRepository> musicRepositoryProvider) {
    this.musicRepositoryProvider = musicRepositoryProvider;
  }

  @Override
  public AlbumDetailViewModel get() {
    return newInstance(musicRepositoryProvider.get());
  }

  public static AlbumDetailViewModel_Factory create(
      Provider<MusicRepository> musicRepositoryProvider) {
    return new AlbumDetailViewModel_Factory(musicRepositoryProvider);
  }

  public static AlbumDetailViewModel newInstance(MusicRepository musicRepository) {
    return new AlbumDetailViewModel(musicRepository);
  }
}
