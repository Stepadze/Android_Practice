package com.example.consecutivepractices.data.di

import androidx.room.Room
import com.example.consecutivepractices.data.api.NetworkModule
import com.example.consecutivepractices.data.database.AppDatabase
import com.example.consecutivepractices.data.preferences.ProfilePreferences
import com.example.consecutivepractices.data.preferences.SearchPreferences
import com.example.consecutivepractices.data.repository.FilmRepositoryImpl
import com.example.consecutivepractices.domain.cache.FilterBadgeCache
import com.example.consecutivepractices.domain.repository.FilmRepository
import com.example.consecutivepractices.domain.usecase.GetFilmByIdUseCase
import com.example.consecutivepractices.domain.usecase.SearchFilmsUseCase
import com.example.consecutivepractices.ui.favorites.FavoritesViewModel
import com.example.consecutivepractices.ui.films.FilmsViewModel
import com.example.consecutivepractices.ui.profile.ProfileViewModel
import com.example.consecutivepractices.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Database
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_database")
            .build()
    }
    single { get<AppDatabase>().favoriteFilmDao() }

    // Preferences
    single { SearchPreferences(androidContext()) }
    single { ProfilePreferences(androidContext()) }

    // Badge Cache
    single { FilterBadgeCache() }

    // Network
    single { NetworkModule.api }

    // Repository
    single<FilmRepository> { FilmRepositoryImpl(get()) }

    // UseCases
    factory { SearchFilmsUseCase(get()) }
    factory { GetFilmByIdUseCase(get()) }

    // ViewModels
    viewModel { FilmsViewModel(get(), get(), get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
}