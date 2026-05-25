package com.example.practice3final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice3final.ui.theme.Practice3FinalTheme

data class Film(
    val id: Int,
    val title: String,
    val year: Int,
    val rating: Double,
    val director: String,
    val description: String
)

fun getMockFilms(): List<Film> = listOf(
    Film(1, "Побег из Шоушенка", 1994, 9.3, "Фрэнк Дарабонт", "Два заключённых находят дружбу и искупление"),
    Film(2, "Крёстный отец", 1972, 9.2, "Фрэнсис Форд Коппола", "Криминальная сага о семье мафиози"),
    Film(3, "Тёмный рыцарь", 2008, 9.0, "Кристофер Нолан", "Бэтмен против Джокера"),
    Film(4, "Криминальное чтиво", 1994, 8.9, "Квентин Тарантино", "Переплетённые истории гангстеров"),
    Film(5, "Список Шиндлера", 1993, 8.9, "Стивен Спилберг", "История спасения евреев во время Холокоста"),
    Film(6, "Властелин колец: Возвращение короля", 2003, 8.9, "Питер Джексон", "Финальная битва за Средиземье"),
    Film(7, "Бойцовский клуб", 1999, 8.8, "Дэвид Финчер", "Подпольный бойцовский клуб меняет жизнь"),
    Film(8, "Начало", 2010, 8.8, "Кристофер Нолан", "Внедрение идеи в сон"),
    Film(9, "Матрица", 1999, 8.7, "Лана Вачовски", "Хакер узнаёт правду о реальности"),
    Film(10, "Форрест Гамп", 1994, 8.8, "Роберт Земекис", "История простого парня из Алабамы"),
    Film(11, "Интерстеллар", 2014, 8.6, "Кристофер Нолан", "Путешествие через червоточину"),
    Film(12, "Зелёная миля", 1999, 8.6, "Фрэнк Дарабонт", "Смертник с необычным даром"),
    Film(13, "Гладиатор", 2000, 8.5, "Ридли Скотт", "Римский генерал становится гладиатором"),
    Film(14, "Социальная сеть", 2010, 7.7, "Дэвид Финчер", "История создания Facebook"),
    Film(15, "Джокер", 2019, 8.4, "Тодд Филлипс", "Становление знаменитого злодея")
)

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
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            FilmListScreen(
                films = getMockFilms(),
                onFilmClick = { filmId ->
                    navController.navigate("detail/$filmId")
                }
            )
        }
        composable("detail/{filmId}") { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")?.toIntOrNull() ?: 1
            val film = getMockFilms().find { it.id == filmId }
            if (film != null) {
                FilmDetailScreen(
                    film = film,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun FilmListScreen(films: List<Film>, onFilmClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(films) { film ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFilmClick(film.id) },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = film.title, fontWeight = FontWeight.Bold)
                    Text(text = "${film.year} • ★ ${film.rating}")
                    Text(text = film.director)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(film: Film, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали фильма") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = film.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(text = "Год выпуска: ${film.year}")
                    Text(text = "Рейтинг: ★ ${film.rating}")
                    Text(text = "Режиссёр: ${film.director}")
                    HorizontalDivider()
                    Text(text = "Описание:", fontWeight = FontWeight.Bold)
                    Text(text = film.description)
                }
            }
        }
    }
}