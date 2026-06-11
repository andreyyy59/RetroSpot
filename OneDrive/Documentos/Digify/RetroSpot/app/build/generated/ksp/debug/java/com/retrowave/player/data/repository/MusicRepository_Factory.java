package com.retrowave.player.data.repository;

import com.retrowave.player.data.local.dao.SongDao;
import com.retrowave.player.data.local.scanner.MediaStoreScanner;
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
public final class MusicRepository_Factory implements Factory<MusicRepository> {
  private final Provider<SongDao> songDaoProvider;

  private final Provider<MediaStoreScanner> scannerProvider;

  public MusicRepository_Factory(Provider<SongDao> songDaoProvider,
      Provider<MediaStoreScanner> scannerProvider) {
    this.songDaoProvider = songDaoProvider;
    this.scannerProvider = scannerProvider;
  }

  @Override
  public MusicRepository get() {
    return newInstance(songDaoProvider.get(), scannerProvider.get());
  }

  public static MusicRepository_Factory create(Provider<SongDao> songDaoProvider,
      Provider<MediaStoreScanner> scannerProvider) {
    return new MusicRepository_Factory(songDaoProvider, scannerProvider);
  }

  public static MusicRepository newInstance(SongDao songDao, MediaStoreScanner scanner) {
    return new MusicRepository(songDao, scanner);
  }
}
