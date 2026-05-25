package com.example.consecutivepractices.data.di

import androidx.room.Room
import com.example.consecutivepractices.data.api.NetworkModule
import com.example.consecutivepractices.data.database.AppDatabase
import com.example.consecutivepractices.data.preferences.SearchPreferences
import com.example.consecutivepractices.data.repository.FilmRepositoryImpl
import com.example.consecutivepractices.domain.cache.FilterBadgeCache
import com.example.consecutivepractices.domain.repository.FilmRepository
import com.example.consecutivepractices.domain.usecase.SearchFilmsUseCase
import com.example.consecutivepractices.ui.favorites.FavoritesViewModel
import com.example.consecutivepractices.ui.films.FilmsViewModel
import com.example.consecutivepractices.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Network
    single { NetworkModule.api }

    // Repository
    single<FilmRepository> { FilmRepositoryImpl(get()) }

    // UseCases
    factory { SearchFilmsUseCase(get()) }

    // Preferences
    single { SearchPreferences(androidContext()) }

    // Cache
    single { FilterBadgeCache() }

    // Database
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favorite_database")
            .build()
    }
    single { get<AppDatabase>().favoriteFilmDao() }

    // ViewModels
    viewModel { FilmsViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
}