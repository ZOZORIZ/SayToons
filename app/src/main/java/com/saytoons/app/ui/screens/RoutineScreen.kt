package com.saytoons.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.saytoons.app.R
import com.saytoons.app.RoutineViewModel
import com.saytoons.app.data.Task
import com.saytoons.app.ui.theme.*

@Composable
fun RoutineScreen(
    viewModel: RoutineViewModel = viewModel(),
    routineId: String,
    globalTimeLeft: String,
    onRoutineComplete: (Int) -> Unit
) {
    LaunchedEffect(routineId) {
        viewModel.loadRoutine(routineId)
    }

    val state by viewModel.uiState.collectAsState()
    val spokenText by viewModel.spokenText.collectAsState()
    val currentTask by viewModel.currentTask.collectAsState()
    val displayImage by viewModel.displayImage.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val starsCollected by viewModel.starsCollected.collectAsState()

    LaunchedEffect(state) {
        if (state == RoutineViewModel.UiState.COMPLETED) {
            onRoutineComplete(starsCollected)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val isListening = state == RoutineViewModel.UiState.LISTENING
        val isSuccess = state == RoutineViewModel.UiState.SUCCESS



        val backgroundRes = if (isSuccess) R.drawable.background_success else R.drawable.background_routine


        Crossfade(targetState = backgroundRes, label = "BgCrossfade", animationSpec = tween(500)) { res ->
            Image(
                painter = painterResource(id = res),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }


        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
                .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.ic_timer), contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = globalTimeLeft,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopAppBar(progress = progress, stars = starsCollected)


            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AnimatedContent(targetState = state, label = "MainContentAnimation") { uiState ->
                    when (uiState) {
                        RoutineViewModel.UiState.SUCCESS -> {
                            SuccessContent(imageUrl = displayImage ?: 0)
                        }
                        RoutineViewModel.UiState.GENERATING -> {
                            GeneratingContent()
                        }
                        RoutineViewModel.UiState.COMPLETED -> {
                            Box(modifier = Modifier.fillMaxSize())
                        }
                        else -> {
                            InstructionContent(
                                task = currentTask,
                                isListening = uiState == RoutineViewModel.UiState.LISTENING
                            )
                        }
                    }
                }
            }


            Box(modifier = Modifier.height(180.dp), contentAlignment = Alignment.Center) {
                if (spokenText.isNotBlank() && !isSuccess) {
                    Text(spokenText, fontSize = 24.sp, fontWeight = FontWeight.Medium, color = TextDarkBlue.copy(alpha = 0.9f), textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 24.dp).offset(y=-100.dp))
                }
            }
        }


        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp)) {
            AnimatedContent(targetState = state, label = "ActionButtonAnimation") {
                when (it) {
                    RoutineViewModel.UiState.SUCCESS -> NextButton(onClick = { viewModel.onNextClicked() })
                    RoutineViewModel.UiState.GENERATING -> { /* Hide buttons */ }
                    RoutineViewModel.UiState.COMPLETED -> { /* Hide buttons */ }
                    else -> MicButton(isListening = isListening, onClick = { viewModel.handleMicClick() })
                }
            }
        }

        AnimatedVisibility(
            visible = state == RoutineViewModel.UiState.LOADING,
            enter = expandHorizontally(expandFrom = Alignment.Start, animationSpec = tween(800, easing = FastOutSlowInEasing)),
            exit = shrinkHorizontally(shrinkTowards = Alignment.End, animationSpec = tween(800, easing = FastOutSlowInEasing))
        ) {
            ThinkingScreenOverlay()
        }
    }
}


@Composable
fun TopAppBar(progress: Float, stars: Int) {
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000), label = "ProgressBar")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .padding(top = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.weight(1f).height(20.dp).clip(CircleShape),
            color = MorningYellow,
            trackColor = Color.White.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_star_large), contentDescription = "Star", modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "x$stars", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextDarkBlue)
        }
    }
}


@Composable
fun InstructionContent(task: Task?, isListening: Boolean) {
    val cardBorderColor by animateColorAsState(
        targetValue = if (isListening) MicButtonGlow.copy(alpha = 0.8f) else Color.White, label = ""
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(color = TaskPillBackground, shape = RoundedCornerShape(50), modifier = Modifier.padding(bottom = 16.dp)) {
            Text(task?.title ?: "", color = TextDarkBlue, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
        }


        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            border = BorderStroke(4.dp, cardBorderColor),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F4F8))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speak),
                        contentDescription = null,
                        tint = SoftBlueButton,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Say this to start:",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))


                    val guideText = task?.expectedSpeech ?: "Say the words!"

                    Text(
                        text = "\"$guideText\"",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = TextDarkBlue,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GeneratingContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(60.dp))
        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            border = BorderStroke(4.dp, SoftBlueButton),
            modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth().height(300.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(Color.White)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = SoftBlueButton, strokeWidth = 6.dp, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("AI is generating your image...", color = SoftBlueButton, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
fun SuccessContent(imageUrl: Any) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(60.dp))
        Card(
            shape = RoundedCornerShape(32.dp), elevation = CardDefaults.cardElevation(8.dp), border = BorderStroke(4.dp, Color.White),
            modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth().height(300.dp).offset(y=90.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(), contentDescription = "Success Image", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SuccessDetails()
    }
}
@Composable
private fun SuccessDetails() {
    Box(contentAlignment = Alignment.Center) {
        val infiniteTransition = rememberInfiniteTransition(label = "PulsingStar")
        val scale by infiniteTransition.animateFloat(initialValue = 1f, targetValue = 1.2f, animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "StarScale")
        Image(painter = painterResource(id = R.drawable.ic_star_large), contentDescription = null, modifier = Modifier.size(400.dp).offset(y=10.dp))
        Text("Great job!", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = TextDarkBlue, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 70.dp))
    }
}
@Composable
fun MicButton(isListening: Boolean, onClick: () -> Unit) {
    val scale = if (isListening) {
        val infiniteTransition = rememberInfiniteTransition(label = "PulsingMic")
        infiniteTransition.animateFloat(
            initialValue = 1f, targetValue = 1.1f,
            animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
            label = "MicScale"
        ).value
    } else 1f
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(110.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        AnimatedContent(targetState = isListening, label = "MicIconAnimation") { listening ->
            val iconRes = if (listening) R.drawable.ic_stop else R.drawable.ic_mic_button
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = if (listening) "Stop" else "Record",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
@Composable
fun NextButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
            .offset(y=70.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_next_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}