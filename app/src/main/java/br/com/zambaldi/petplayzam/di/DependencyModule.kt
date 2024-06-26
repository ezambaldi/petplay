package br.com.zambaldi.petplayzam.di

import androidx.room.Room
import br.com.zambaldi.petplayzam.providers.CoroutineContextProvider
import br.com.zambaldi.petplayzam.providers.datasource.local.AppDatabase
import br.com.zambaldi.petplayzam.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplayzam.providers.datasource.local.api.PetPlayLocalProviderImpl
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalAudioGroupRepository
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalAudioGroupRepositoryImpl
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalAudioRepository
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalAudioRepositoryImpl
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalGroupRepository
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalGroupRepositoryImpl
import br.com.zambaldi.petplayzam.providers.mappers.AudioGroupMapper
import br.com.zambaldi.petplayzam.providers.mappers.AudioMapper
import br.com.zambaldi.petplayzam.providers.mappers.GroupMapper
import br.com.zambaldi.petplayzam.ui.audios.AudiosViewModel
import br.com.zambaldi.petplayzam.ui.groups.GroupsViewModel
import br.com.zambaldi.petplayzam.ui.play.PlayViewModel
import br.com.zambaldi.petplayzam.ui.recorders.AndroidAudioPlayer
import br.com.zambaldi.petplayzam.usecases.AudioGroupUseCase
import br.com.zambaldi.petplayzam.usecases.AudioGroupUseCaseImpl
import br.com.zambaldi.petplayzam.usecases.AudioUseCase
import br.com.zambaldi.petplayzam.usecases.AudioUseCaseImpl
import br.com.zambaldi.petplayzam.usecases.GroupUseCase
import br.com.zambaldi.petplayzam.usecases.GroupUseCaseImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyModule {

    private val dbModule = module {
        single {
            Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "pet_play_db").allowMainThreadQueries().fallbackToDestructiveMigration().build()
        }
        single { get<AppDatabase>().audioDAO() }
        single { get<AppDatabase>().groupDAO() }
        single { get<AppDatabase>().audiosGroupDAO() }
    }

    private val useCaseModule = module {
        factory<AudioUseCase> {
            AudioUseCaseImpl(get(), get())
        }
        factory<GroupUseCase> {
            GroupUseCaseImpl(get(), get(), get(), get())
        }
        factory<AudioGroupUseCase> {
            AudioGroupUseCaseImpl(get(), get())
        }
    }

    private val providerModule = module {

        single<AndroidAudioPlayer> {
            AndroidAudioPlayer(get())
        }

        single<PetPlayLocalProviderApi> {
            PetPlayLocalProviderImpl(get(), get(), get())
        }
        factory { AudioMapper }
        factory { GroupMapper }
        factory { AudioGroupMapper }
        factory<CoroutineContextProvider> {
            CoroutineContextProvider.Default()
        }
        factory<LocalAudioRepository> { LocalAudioRepositoryImpl(get(), get()) }
        factory<LocalGroupRepository> { LocalGroupRepositoryImpl(get(), get()) }
        factory<LocalAudioGroupRepository> { LocalAudioGroupRepositoryImpl(get(), get()) }
    }

    private val viewModelModule = module {
        viewModel { AudiosViewModel(get()) }
        viewModel { GroupsViewModel(get(), get(), get()) }
        viewModel { PlayViewModel(get(), get()) }
    }

    val appModules = dbModule + useCaseModule + providerModule + viewModelModule
}