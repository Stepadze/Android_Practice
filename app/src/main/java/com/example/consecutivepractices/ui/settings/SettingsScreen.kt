package com.example.consecutivepractices.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onApply: (String) -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val savedQuery by viewModel.searchQuery.collectAsState()
    val savedType by viewModel.searchType.collectAsState()
    val savedYear by viewModel.searchYear.collectAsState()

    var query by remember(savedQuery) { mutableStateOf(savedQuery) }
    var type by remember(savedType) { mutableStateOf(savedType) }
    var year by remember(savedYear) { mutableStateOf(savedYear) }

    val typeOptions = listOf("movie", "series", "episode")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Настройки поиска") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Поисковый запрос", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Название фильма") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text("Тип", style = MaterialTheme.typography.titleMedium)
            typeOptions.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = type == option,
                        onClick = { type = option }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (option) {
                            "movie" -> "Фильм"
                            "series" -> "Сериал"
                            "episode" -> "Эпизод"
                            else -> option
                        }
                    )
                }
            }

            Text("Год выпуска", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Например: 2020") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.saveSettings(query, type, year)
                    onApply(query)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Применить")
            }
        }
    }
}