package com.example.consecutivepractices.ui.films

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmApp(paddingValues: PaddingValues) {
    val viewModel: FilmsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is FilmsUiState.Loading -> {
                CircularProgressIndicator()
            }
            is FilmsUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ошибка: ${(uiState as FilmsUiState.Error).message}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.searchFilms("Batman") }) {
                        Text("Повторить")
                    }
                }
            }
            is FilmsUiState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((uiState as FilmsUiState.Success).films) { film ->
                        val isFavorite by viewModel.isFavorite(film.imdbId).collectAsState(initial = false)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = film.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = film.year,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (isFavorite) {
                                            viewModel.removeFromFavorites(film.imdbId)
                                        } else {
                                            viewModel.addToFavorites(film)
                                        }
                                    }
                                ) {
                                    Icon(
                                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}