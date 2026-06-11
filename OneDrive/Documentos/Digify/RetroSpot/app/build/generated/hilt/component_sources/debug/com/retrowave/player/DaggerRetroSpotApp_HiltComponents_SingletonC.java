package com.retrowave.player;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.media3.exoplayer.ExoPlayer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.retrowave.player.data.ai.GeminiApi;
import com.retrowave.player.data.ai.RecommendationEngine;
import com.retrowave.player.data.download.SpotifyDownloadApi;
import com.retrowave.player.data.download.SpotifyDownloadRepository;
import com.retrowave.player.data.download.SpotifySearchApi;
import com.retrowave.player.data.download.SpotifySearchRepository;
import com.retrowave.player.data.local.dao.PlaylistDao;
import com.retrowave.player.data.local.dao.SongDao;
import com.retrowave.player.data.local.db.AppDatabase;
import com.retrowave.player.data.local.scanner.MediaStoreScanner;
import com.retrowave.player.data.repository.MusicRepository;
import com.retrowave.player.data.repository.PlaylistRepository;
import com.retrowave.player.di.AiModule_ProvideGeminiApiFactory;
import com.retrowave.player.di.AiModule_ProvideOkHttpClientFactory;
import com.retrowave.player.di.AiModule_ProvideRetrofitFactory;
import com.retrowave.player.di.AppModule_ProvideExoPlayerFactory;
import com.retrowave.player.di.DatabaseModule_ProvideDatabaseFactory;
import com.retrowave.player.di.DatabaseModule_ProvidePlaylistDaoFactory;
import com.retrowave.player.di.DatabaseModule_ProvideSongDaoFactory;
import com.retrowave.player.di.SpotifyDownloadModule_ProvideFileDownloadClientFactory;
import com.retrowave.player.di.SpotifyDownloadModule_ProvideSpotifyDownloadApiFactory;
import com.retrowave.player.di.SpotifySearchModule_ProvideSpotifySearchApiFactory;
import com.retrowave.player.di.SpotifySearchModule_ProvideSpotifySearchRetrofitFactory;
import com.retrowave.player.ui.home.HomeViewModel;
import com.retrowave.player.ui.home.HomeViewModel_HiltModules_KeyModule_ProvideFactory;
import com.retrowave.player.ui.library.LibraryViewModel;
import com.retrowave.player.ui.library.LibraryViewModel_HiltModules_KeyModule_ProvideFactory;
import com.retrowave.player.ui.player.PlayerViewModel;
import com.retrowave.player.ui.player.PlayerViewModel_HiltModules_KeyModule_ProvideFactory;
import com.retrowave.player.ui.playlist.AlbumDetailViewModel;
import com.retrowave.player.ui.playlist.AlbumDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.retrowave.player.ui.playlist.PlaylistDetailViewModel;
import com.retrowave.player.ui.playlist.PlaylistDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.retrowave.player.ui.search.SearchViewModel;
import com.retrowave.player.ui.search.SearchViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerRetroSpotApp_HiltComponents_SingletonC {
  private DaggerRetroSpotApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public RetroSpotApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements RetroSpotApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements RetroSpotApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements RetroSpotApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements RetroSpotApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements RetroSpotApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements RetroSpotApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements RetroSpotApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public RetroSpotApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends RetroSpotApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends RetroSpotApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends RetroSpotApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends RetroSpotApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return ImmutableSet.<String>of(AlbumDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide(), HomeViewModel_HiltModules_KeyModule_ProvideFactory.provide(), LibraryViewModel_HiltModules_KeyModule_ProvideFactory.provide(), PlayerViewModel_HiltModules_KeyModule_ProvideFactory.provide(), PlaylistDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide(), SearchViewModel_HiltModules_KeyModule_ProvideFactory.provide());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends RetroSpotApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AlbumDetailViewModel> albumDetailViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LibraryViewModel> libraryViewModelProvider;

    private Provider<PlayerViewModel> playerViewModelProvider;

    private Provider<PlaylistDetailViewModel> playlistDetailViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.albumDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.libraryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.playerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.playlistDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(6).put("com.retrowave.player.ui.playlist.AlbumDetailViewModel", ((Provider) albumDetailViewModelProvider)).put("com.retrowave.player.ui.home.HomeViewModel", ((Provider) homeViewModelProvider)).put("com.retrowave.player.ui.library.LibraryViewModel", ((Provider) libraryViewModelProvider)).put("com.retrowave.player.ui.player.PlayerViewModel", ((Provider) playerViewModelProvider)).put("com.retrowave.player.ui.playlist.PlaylistDetailViewModel", ((Provider) playlistDetailViewModelProvider)).put("com.retrowave.player.ui.search.SearchViewModel", ((Provider) searchViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<String, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.retrowave.player.ui.playlist.AlbumDetailViewModel 
          return (T) new AlbumDetailViewModel(singletonCImpl.musicRepositoryProvider.get());

          case 1: // com.retrowave.player.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.musicRepositoryProvider.get(), singletonCImpl.playlistRepositoryProvider.get(), singletonCImpl.recommendationEngineProvider.get());

          case 2: // com.retrowave.player.ui.library.LibraryViewModel 
          return (T) new LibraryViewModel(singletonCImpl.musicRepositoryProvider.get(), singletonCImpl.playlistRepositoryProvider.get());

          case 3: // com.retrowave.player.ui.player.PlayerViewModel 
          return (T) new PlayerViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.musicRepositoryProvider.get(), singletonCImpl.provideExoPlayerProvider.get());

          case 4: // com.retrowave.player.ui.playlist.PlaylistDetailViewModel 
          return (T) new PlaylistDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.playlistRepositoryProvider.get(), singletonCImpl.musicRepositoryProvider.get());

          case 5: // com.retrowave.player.ui.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.musicRepositoryProvider.get(), singletonCImpl.spotifyDownloadRepositoryProvider.get(), singletonCImpl.spotifySearchRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends RetroSpotApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends RetroSpotApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends RetroSpotApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<MediaStoreScanner> mediaStoreScannerProvider;

    private Provider<MusicRepository> musicRepositoryProvider;

    private Provider<PlaylistRepository> playlistRepositoryProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<GeminiApi> provideGeminiApiProvider;

    private Provider<RecommendationEngine> recommendationEngineProvider;

    private Provider<ExoPlayer> provideExoPlayerProvider;

    private Provider<SpotifyDownloadApi> provideSpotifyDownloadApiProvider;

    private Provider<OkHttpClient> provideFileDownloadClientProvider;

    private Provider<SpotifyDownloadRepository> spotifyDownloadRepositoryProvider;

    private Provider<Retrofit> provideSpotifySearchRetrofitProvider;

    private Provider<SpotifySearchApi> provideSpotifySearchApiProvider;

    private Provider<SpotifySearchRepository> spotifySearchRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private SongDao songDao() {
      return DatabaseModule_ProvideSongDaoFactory.provideSongDao(provideDatabaseProvider.get());
    }

    private PlaylistDao playlistDao() {
      return DatabaseModule_ProvidePlaylistDaoFactory.providePlaylistDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 1));
      this.mediaStoreScannerProvider = DoubleCheck.provider(new SwitchingProvider<MediaStoreScanner>(singletonCImpl, 2));
      this.musicRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MusicRepository>(singletonCImpl, 0));
      this.playlistRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PlaylistRepository>(singletonCImpl, 3));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 7));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 6));
      this.provideGeminiApiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiApi>(singletonCImpl, 5));
      this.recommendationEngineProvider = DoubleCheck.provider(new SwitchingProvider<RecommendationEngine>(singletonCImpl, 4));
      this.provideExoPlayerProvider = DoubleCheck.provider(new SwitchingProvider<ExoPlayer>(singletonCImpl, 8));
      this.provideSpotifyDownloadApiProvider = DoubleCheck.provider(new SwitchingProvider<SpotifyDownloadApi>(singletonCImpl, 10));
      this.provideFileDownloadClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 11));
      this.spotifyDownloadRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SpotifyDownloadRepository>(singletonCImpl, 9));
      this.provideSpotifySearchRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 14));
      this.provideSpotifySearchApiProvider = DoubleCheck.provider(new SwitchingProvider<SpotifySearchApi>(singletonCImpl, 13));
      this.spotifySearchRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SpotifySearchRepository>(singletonCImpl, 12));
    }

    @Override
    public void injectRetroSpotApp(RetroSpotApp retroSpotApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.retrowave.player.data.repository.MusicRepository 
          return (T) new MusicRepository(singletonCImpl.songDao(), singletonCImpl.mediaStoreScannerProvider.get());

          case 1: // com.retrowave.player.data.local.db.AppDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.retrowave.player.data.local.scanner.MediaStoreScanner 
          return (T) new MediaStoreScanner(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.retrowave.player.data.repository.PlaylistRepository 
          return (T) new PlaylistRepository(singletonCImpl.playlistDao());

          case 4: // com.retrowave.player.data.ai.RecommendationEngine 
          return (T) new RecommendationEngine(singletonCImpl.provideGeminiApiProvider.get());

          case 5: // com.retrowave.player.data.ai.GeminiApi 
          return (T) AiModule_ProvideGeminiApiFactory.provideGeminiApi(singletonCImpl.provideRetrofitProvider.get());

          case 6: // retrofit2.Retrofit 
          return (T) AiModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 7: // okhttp3.OkHttpClient 
          return (T) AiModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 8: // androidx.media3.exoplayer.ExoPlayer 
          return (T) AppModule_ProvideExoPlayerFactory.provideExoPlayer(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.retrowave.player.data.download.SpotifyDownloadRepository 
          return (T) new SpotifyDownloadRepository(singletonCImpl.provideSpotifyDownloadApiProvider.get(), singletonCImpl.musicRepositoryProvider.get(), singletonCImpl.provideFileDownloadClientProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.retrowave.player.data.download.SpotifyDownloadApi 
          return (T) SpotifyDownloadModule_ProvideSpotifyDownloadApiFactory.provideSpotifyDownloadApi();

          case 11: // @javax.inject.Named("file_download") okhttp3.OkHttpClient 
          return (T) SpotifyDownloadModule_ProvideFileDownloadClientFactory.provideFileDownloadClient();

          case 12: // com.retrowave.player.data.download.SpotifySearchRepository 
          return (T) new SpotifySearchRepository(singletonCImpl.provideSpotifySearchApiProvider.get(), singletonCImpl.provideFileDownloadClientProvider.get());

          case 13: // com.retrowave.player.data.download.SpotifySearchApi 
          return (T) SpotifySearchModule_ProvideSpotifySearchApiFactory.provideSpotifySearchApi(singletonCImpl.provideSpotifySearchRetrofitProvider.get());

          case 14: // @javax.inject.Named("spotify_search") retrofit2.Retrofit 
          return (T) SpotifySearchModule_ProvideSpotifySearchRetrofitFactory.provideSpotifySearchRetrofit(singletonCImpl.provideFileDownloadClientProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
