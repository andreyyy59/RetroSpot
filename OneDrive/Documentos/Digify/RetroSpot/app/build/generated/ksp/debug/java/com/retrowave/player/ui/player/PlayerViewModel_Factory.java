package com.retrowave.player.ui.player;

import android.content.Context;
import androidx.media3.exoplayer.ExoPlayer;
import com.retrowave.player.data.repository.MusicRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<MusicRepository> musicRepositoryProvider;

  private final Provider<ExoPlayer> playerProvider;

  public PlayerViewModel_Factory(Provider<Context> contextProvider,
      Provider<MusicRepository> musicRepositoryProvider, Provider<ExoPlayer> playerProvider) {
    this.contextProvider = contextProvider;
    this.musicRepositoryProvider = musicRepositoryProvider;
    this.playerProvider = playerProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(contextProvider.get(), musicRepositoryProvider.get(), playerProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<Context> contextProvider,
      Provider<MusicRepository> musicRepositoryProvider, Provider<ExoPlayer> playerProvider) {
    return new PlayerViewModel_Factory(contextProvider, musicRepositoryProvider, playerProvider);
  }

  public static PlayerViewModel newInstance(Context context, MusicRepository musicRepository,
      ExoPlayer player) {
    return new PlayerViewModel(context, musicRepository, player);
  }
}
