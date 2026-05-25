package com.example.practice3final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practice3final.data.api.OmdbApi
import com.example.practice3final.data.network.RetrofitInstance
import com.example.practice3final.domain.model.Film
import com.example.practice3final.ui.theme.Practice3FinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practice3FinalTheme {
                FilmApp()
            }
        }
    }
}

@Composable
fun FilmApp() {
    var films by remember { mutableStateOf<List<Film>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val api: OmdbApi = RetrofitInstance.api
            val response = api.searchFilms("Batman")

            if (response.response != "True") {
                throw Exception(response.error ?: "Nothing found")
            }

            films = response.films?.map { filmShort ->
                Film(
                    id = filmShort.imdbId,
                    title = filmShort.title,
                    year = filmShort.year,
                    posterUrl = filmShort.posterUrl
                )
            } ?: emptyList()

        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: $errorMessage")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {}) {
                        Text("Retry")
                    }
                }
            }
        }
        films.isNotEmpty() -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(films) { film ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = film.title)
                            Text(text = film.year)
                        }
                    }
                }
            }
        }
    }
}