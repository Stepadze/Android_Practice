package com.example.consecutivepractices.ui.films

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consecutivepractices.domain.model.FilmShort
import org.koin.androidx.compose.koinViewModel
import com.example.consecutivepractices.data.database.FavoriteFilmEntity
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmsListScreen(
    onFilmClick: (String) -> Unit,
    viewModel: FilmsViewModel = koinViewModel()
) {
    val uiState by viewModel.filmsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchType by viewModel.searchType.collectAsState()
    val searchYear by viewModel.searchYear.collectAsState()

    var textFieldValue by remember { mutableStateOf(searchQuery) }
    var isTypeMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Фон
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        )

        // Панель фильтров (по центру сверху)
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopCenter)
                .width(320.dp) // фиксированная ширина
                .padding(top = 30.dp)
        ) {
            // Поле поиска
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Поиск") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { viewModel.searchFilms(textFieldValue, searchType, searchYear) }
                )
            )

            // Выпадающий список типов
            DropdownMenu(
                expanded = isTypeMenuExpanded,
                onDismissRequest = { isTypeMenuExpanded = false }
            ) {
                listOf("movie", "series").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            viewModel._searchType.value = type
                            isTypeMenuExpanded = false
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Тип:", modifier = Modifier.weight(1f))
                Surface(
                    modifier = Modifier.clickable { isTypeMenuExpanded = !isTypeMenuExpanded },
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(searchType, modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Развернуть список типов"
                        )
                    }
                }
            }

            // Поле года выпуска
            OutlinedTextField(
                value = searchYear,
                onValueChange = { viewModel._searchYear.value = it },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 5.dp),
                label = { Text("Год выпуска") }
            )

            // ОТДЕЛЬНАЯ КНОПКА ПОИСКА — СНИЗУ ПОСЛЕ ПОЛЯ ГОДА
            Button(
                onClick = {
                    viewModel.searchFilms(textFieldValue, searchType, searchYear)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Найти")
            }
        }

        // Список фильмов (по центру под фильтрами)
        when (val state = uiState) {
            is FilmsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is FilmsUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Ошибка: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.searchFilms(textFieldValue, searchType, searchYear)
                        }) {
                            Text("Повторить")
                        }
                    }
                }
            }
            is FilmsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 370.dp, start = 16.dp, end = 16.dp) // отступ сверху под фильтры и кнопку поиска
                ) {
                    items(state.films) { film ->
                        FilmItem(film = film, onClick = { onFilmClick(film.imdbId) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}



@Composable
fun FilmItem(film: FilmShort, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = film.year.take(4),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = film.year,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}