package br.com.zambaldi.petplay.di

import androidx.room.Room
import br.com.zambaldi.petplay.providers.CoroutineContextProvider
import br.com.zambaldi.petplay.providers.datasource.local.AppDatabase
import br.com.zambaldi.petplay.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplay.providers.datasource.local.api.PetPlayLocalProviderImpl
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioGroupRepository
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioGroupRepositoryImpl
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioRepository
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioRepositoryImpl
import br.com.zambaldi.petplay.providers.datasource.repository.LocalGroupRepository
import br.com.zambaldi.petplay.providers.datasource.repository.LocalGroupRepositoryImpl
import br.com.zambaldi.petplay.providers.mappers.AudioGroupMapper
import br.com.zambaldi.petplay.providers.mappers.AudioMapper
import br.com.zambaldi.petplay.providers.mappers.GroupMapper
import br.com.zambaldi.petplay.ui.audios.AudiosViewModel
import br.com.zambaldi.petplay.usecases.AudioGroupUseCase
import br.com.zambaldi.petplay.usecases.AudioGroupUseCaseImpl
import br.com.zambaldi.petplay.usecases.AudioUseCase
import br.com.zambaldi.petplay.usecases.AudioUseCaseImpl
import br.com.zambaldi.petplay.usecases.GroupUseCase
import br.com.zambaldi.petplay.usecases.GroupUseCaseImpl
import io.reactivex.schedulers.Schedulers.single
import org.koin.androidx.viewmodel.compat.ScopeCompat.viewModel
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
            AudioUseCaseImpl(get(), get(), get())
        }
        factory<GroupUseCase> {
            GroupUseCaseImpl(get(), get())
        }
        factory<AudioGroupUseCase> {
            AudioGroupUseCaseImpl(get(), get())
        }
    }

    private val providerModule = module {
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
        factory<LocalGroupRepository> { LocalGroupRepositoryImpl(get()) }
        factory<LocalAudioGroupRepository> { LocalAudioGroupRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModel { AudiosViewModel(get()) }
    }

    val appModules = dbModule + useCaseModule + providerModule + viewModelModule
}