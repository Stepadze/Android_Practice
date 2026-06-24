package com.example.consecutivepractices.ui.profile

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.consecutivepractices.data.preferences.UserProfile
import com.example.consecutivepractices.notifications.NotificationScheduler
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    var name by remember(profile.name) { mutableStateOf(profile.name) }
    var position by remember(profile.position) { mutableStateOf(profile.position) }
    var resumeUrl by remember(profile.resumeUrl) { mutableStateOf(profile.resumeUrl) }
    var avatarUri by remember(profile.avatarUri) { mutableStateOf(profile.avatarUri) }
    var pairTime by remember(profile.pairTime) { mutableStateOf(profile.pairTime) }
    var pairTimeError by remember { mutableStateOf(false) }

    var showImageSourceDialog by remember { mutableStateOf(false) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var permissionDenied by remember { mutableStateOf(false) }

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> if (!granted) permissionDenied = true }

    LaunchedEffect(permissionDenied) {
        if (permissionDenied) onBack()
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, storagePermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            storagePermissionLauncher.launch(storagePermission)
        }
    }

    // Запрос разрешения на уведомления (Android 13+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* просто запросили */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { avatarUri = it.toString() } }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) cameraImageUri?.let { avatarUri = it.toString() } }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val photoFile = File.createTempFile("avatar_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", photoFile
            )
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        }
    }

    // TimePicker диалог
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            pairTime = String.format("%02d:%02d", hour, minute)
            pairTimeError = false
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    // Валидация времени
    fun isValidTime(time: String): Boolean {
        return time.matches(Regex("^([01]\\d|2[0-3]):[0-5]\\d$"))
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Выбрать фото") },
            text = { Text("Откуда загрузить фото?") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    if (ContextCompat.checkSelfPermission(context, storagePermission)
                        == PackageManager.PERMISSION_GRANTED
                    ) galleryLauncher.launch("image/*")
                    else storagePermissionLauncher.launch(storagePermission)
                }) { Text("Галерея") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        val photoFile = File.createTempFile("avatar_", ".jpg", context.cacheDir)
                        val uri = FileProvider.getUriForFile(
                            context, "${context.packageName}.provider", photoFile
                        )
                        cameraImageUri = uri
                        cameraLauncher.launch(uri)
                    } else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }) { Text("Камера") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri.isNotEmpty()) {
                    AsyncImage(
                        model = Uri.parse(avatarUri),
                        contentDescription = "Аватар",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = "Нажмите для смены фото",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = position,
                onValueChange = { position = it },
                label = { Text("Должность") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("Ссылка на резюме (URL)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Поле времени пары
            OutlinedTextField(
                value = pairTime,
                onValueChange = {
                    pairTime = it
                    pairTimeError = it.isNotEmpty() && !isValidTime(it)
                },
                label = { Text("Время любимой пары (HH:mm)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = pairTimeError,
                supportingText = {
                    if (pairTimeError) {
                        Text(
                            text = "Неверный формат. Используйте HH:mm",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { timePickerDialog.show() }) {
                        Icon(Icons.Default.Schedule, contentDescription = "Выбрать время")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (pairTime.isNotEmpty() && !isValidTime(pairTime)) {
                        pairTimeError = true
                        return@Button
                    }
                    viewModel.saveProfile(
                        UserProfile(
                            name = name,
                            position = position,
                            resumeUrl = resumeUrl,
                            avatarUri = avatarUri,
                            pairTime = pairTime
                        )
                    )
                    // Планируем уведомление если время указано
                    if (pairTime.isNotEmpty() && isValidTime(pairTime)) {
                        val parts = pairTime.split(":")
                        NotificationScheduler.scheduleNotification(
                            context = context,
                            hour = parts[0].toInt(),
                            minute = parts[1].toInt(),
                            name = name
                        )
                    }
                    onBack()
                },
                enabled = !pairTimeError,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }
    }
}