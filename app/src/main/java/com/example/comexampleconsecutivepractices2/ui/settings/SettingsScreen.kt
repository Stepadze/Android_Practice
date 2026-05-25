package com.example.consecutivepractices.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onApply: (String, String, String) -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val settings by viewModel.searchSettings.collectAsStateWithLifecycle()

    var query by remember(settings.query) { mutableStateOf(settings.query) }
    var selectedType by remember(settings.type) { mutableStateOf(settings.type) }
    var year by remember(settings.year) { mutableStateOf(settings.year) }

    val typeOptions = listOf("movie", "series", "episode")
    val typeLabels = mapOf(
        "movie" to "Фильмы",
        "series" to "Сериалы",
        "episode" to "Эпизоды"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки поиска") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Поисковый запрос",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Название фильма") },
                placeholder = { Text("Например: Batman") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "Тип контента",
                style = MaterialTheme.typography.titleMedium
            )

            typeOptions.forEach { type ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == type,
                        onClick = { selectedType = type }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(typeLabels[type] ?: type)
                }
            }

            Text(
                text = "Год выпуска",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Год") },
                placeholder = { Text("Например: 2020") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveSettings(query, selectedType, year)
                    onApply(query, selectedType, year)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Применить")
            }
        }
    }
}