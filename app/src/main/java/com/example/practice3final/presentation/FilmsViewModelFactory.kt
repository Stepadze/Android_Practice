package com.example.practice3final.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practice3final.domain.usecase.GetAllFilmsUseCase
import com.example.practice3final.domain.usecase.GetFilmByIdUseCase

class FilmsViewModelFactory(
    private val getAllFilmsUseCase: GetAllFilmsUseCase,
    private val getFilmByIdUseCase: GetFilmByIdUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmsViewModel(getAllFilmsUseCase, getFilmByIdUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}