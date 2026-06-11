package com.retrowave.player.di;

import com.retrowave.player.data.local.dao.PlaylistDao;
import com.retrowave.player.data.local.db.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvidePlaylistDaoFactory implements Factory<PlaylistDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvidePlaylistDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PlaylistDao get() {
    return providePlaylistDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePlaylistDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePlaylistDaoFactory(databaseProvider);
  }

  public static PlaylistDao providePlaylistDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePlaylistDao(database));
  }
}
