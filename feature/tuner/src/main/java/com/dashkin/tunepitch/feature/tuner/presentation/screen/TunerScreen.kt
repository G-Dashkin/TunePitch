package com.dashkin.tunepitch.feature.tuner.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashkin.tunepitch.core.ui.theme.NeonBackground
import com.dashkin.tunepitch.core.ui.theme.NeonCyan
import com.dashkin.tunepitch.core.ui.theme.NeonTextSecondary
import com.dashkin.tunepitch.core.ui.theme.NeonTextTertiary
import com.dashkin.tunepitch.core.ui.theme.PitchGreen
import com.dashkin.tunepitch.core.ui.theme.PitchRed
import com.dashkin.tunepitch.core.ui.theme.PitchYellow
import com.dashkin.tunepitch.feature.tuner.presentation.component.NoteDisplay
import com.dashkin.tunepitch.feature.tuner.presentation.component.PitchMeter
import com.dashkin.tunepitch.feature.tuner.presentation.state.TunerError
import com.dashkin.tunepitch.feature.tuner.presentation.state.TunerEvent
import com.dashkin.tunepitch.feature.tuner.presentation.viewmodel.TunerViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TunerScreen(onNavigateBack: () -> Unit) {
    val viewModel: TunerViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.onEvent(TunerEvent.OnPermissionGranted)
        else viewModel.onEvent(TunerEvent.OnPermissionDenied)
    }

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) viewModel.onEvent(TunerEvent.OnPermissionGranted)
        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Free Tuner",
                        style = MaterialTheme.typography.titleLarge,
                        color = NeonCyan
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NeonBackground)
            )
        },
        containerColor = NeonBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.error) {
                TunerError.NoPermission -> NoPermissionContent(
                    onRetry = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }
                )
                TunerError.MicrophoneUnavailable -> MicrophoneErrorContent()
                null -> TunerContent(
                    centsOffset = state.pitchResult?.centsOffset ?: 0f,
                    pitchResult = state.pitchResult,
                    isRecording = state.isRecording
                )
            }
        }
    }
}

@Composable
private fun TunerContent(
    centsOffset: Float,
    pitchResult: com.dashkin.tunepitch.core.audio.model.PitchResult?,
    isRecording: Boolean
) {
    val centsLabel = buildCentsLabel(pitchResult?.centsOffset)
    val centsColor = buildCentsColor(pitchResult?.centsOffset)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        NoteDisplay(pitchResult = pitchResult)

        Spacer(modifier = Modifier.height(32.dp))

        PitchMeter(
            centsOffset = centsOffset,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(2f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "♭ FLAT", style = MaterialTheme.typography.labelSmall, color = NeonTextTertiary)
            Text(text = "0", style = MaterialTheme.typography.labelSmall, color = NeonTextTertiary)
            Text(text = "SHARP ♯", style = MaterialTheme.typography.labelSmall, color = NeonTextTertiary)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = centsLabel,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = centsColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isRecording && pitchResult == null) "Listening..." else "",
            style = MaterialTheme.typography.bodyMedium,
            color = NeonTextSecondary
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun NoPermissionContent(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.MicOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = PitchRed
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Microphone Access Required",
            style = MaterialTheme.typography.headlineSmall,
            color = NeonCyan,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "TunePitch needs microphone access to detect your pitch in real time.",
            style = MaterialTheme.typography.bodyMedium,
            color = NeonTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        androidx.compose.material3.OutlinedButton(
            onClick = onRetry,
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
        ) {
            Text(text = "Grant Permission", color = NeonCyan)
        }
    }
}

@Composable
private fun MicrophoneErrorContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.MicOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = PitchRed
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Microphone Unavailable",
            style = MaterialTheme.typography.headlineSmall,
            color = PitchRed,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Could not access the microphone. Please check that no other app is using it.",
            style = MaterialTheme.typography.bodyMedium,
            color = NeonTextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

private fun buildCentsLabel(centsOffset: Float?): String = when {
    centsOffset == null -> ""
    abs(centsOffset) < 5f -> "In Tune ♫"
    centsOffset < 0f -> "${centsOffset.roundToInt()}¢  flat"
    else -> "+${centsOffset.roundToInt()}¢  sharp"
}

private fun buildCentsColor(centsOffset: Float?) = when {
    centsOffset == null -> NeonTextTertiary
    abs(centsOffset) <= 10f -> PitchGreen
    abs(centsOffset) <= 25f -> PitchYellow
    else -> PitchRed
}
