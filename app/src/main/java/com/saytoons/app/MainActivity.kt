package com.saytoons.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.saytoons.app.data.Kid
import com.saytoons.app.data.ParentRepository
import com.saytoons.app.ui.screens.*
import com.saytoons.app.ui.theme.SayToonsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import android.media.MediaPlayer
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest


sealed class Screen(val route: String) {
    object Entry : Screen("entry")
    object Login : Screen("login")
    object CreateParentAccount : Screen("create_parent_account")
    object ParentDashboard : Screen("parent_dashboard")
    object KidHome : Screen("kid_home")
    object RoutineSelection : Screen("routine_selection")
    data class Routine(val routineId: String) : Screen("routine/{routineId}")
    object Rewards : Screen("rewards")
    object AddKid : Screen("add_kid")
    object ParentLock : Screen("parent_lock")
    object EndRoutine : Screen("end_routine")
    object MiniGame : Screen("mini_game")
}

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parentRepository = ParentRepository()
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)


        ScreenTimeManager.init(this)

        setContent {
            SayToonsTheme {
                var currentScreen: Screen by remember { mutableStateOf(Screen.Entry) }
                var showLoadingOverlay by remember { mutableStateOf(false) }
                var showGuestLoginDialog by remember { mutableStateOf(false) }

                val shouldClose by ScreenTimeManager.shouldCloseApp.collectAsState()
                val showWarning by ScreenTimeManager.showWarning.collectAsState()
                val timeLeftGlobal by ScreenTimeManager.timeLeftFormatted.collectAsState()

                val gso = remember {
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                }

                val googleSignInClient = remember {
                    GoogleSignIn.getClient(this, gso)
                }

                val loginViewModel: LoginViewModel = viewModel()

                val googleLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        account?.idToken?.let { token ->
                            loginViewModel.googleLogin(token)
                        }
                    } catch (e: ApiException) {
                        e.printStackTrace()
                    }
                }



                val scope = rememberCoroutineScope()

                var allKids by remember { mutableStateOf<List<Kid>>(emptyList()) }
                var selectedKid by remember { mutableStateOf<Kid?>(null) }
                var authTrigger by remember { mutableStateOf(0) }

                LaunchedEffect(authTrigger) {
                    if (parentRepository.isUserLoggedIn()) {
                        parentRepository.getKidsFlow()?.collect { kidsList ->
                            allKids = kidsList
                            if (selectedKid == null && kidsList.isNotEmpty()) {
                                selectedKid = kidsList[0]
                            } else if (selectedKid != null) {
                                selectedKid = kidsList.find { it.id == selectedKid?.id } ?: kidsList.firstOrNull()
                            }
                        }
                    } else {
                        allKids = emptyList()
                        selectedKid = null
                    }
                }

                fun navigateWithLoading(destination: Screen) {
                    if (showLoadingOverlay) return
                    scope.launch {
                        showLoadingOverlay = true
                        delay(1500)
                        currentScreen = destination
                        showLoadingOverlay = false
                    }
                }


                if (shouldClose) {
                    ByeByeScreen()
                } else {
                    //app content
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (val screen = currentScreen) {
                            is Screen.Entry -> {
                                EntryScreen(
                                    kids = allKids,
                                    selectedKid = selectedKid,
                                    onKidSelected = { kid -> selectedKid = kid },
                                    onKidModeClick = {

                                        if (ScreenTimeManager.isKidModeLocked.value) {

                                        } else if (parentRepository.isUserLoggedIn()) {

                                            ScreenTimeManager.startTimer()
                                            navigateWithLoading(Screen.KidHome)
                                        } else {

                                            showGuestLoginDialog = true
                                        }

                                    },
                                    onParentModeClick = {
                                        navigateWithLoading(Screen.ParentLock)
                                    }
                                )
                            }
                            is Screen.ParentLock -> {
                                ParentLockScreen(
                                    onUnlockSuccess = {
                                        scope.launch {
                                            showLoadingOverlay = true
                                            delay(1000)
                                            currentScreen = if (parentRepository.isUserLoggedIn()) {
                                                Screen.ParentDashboard
                                            } else {
                                                Screen.Login
                                            }
                                            showLoadingOverlay = false
                                        }
                                    },
                                    onUnlockFailed = { currentScreen = Screen.Entry }
                                )
                            }
                            is Screen.Login -> {
                                val loginViewModel: LoginViewModel = viewModel()
                                LoginScreen(
                                    viewModel = loginViewModel,
                                    onLoginSuccess = {
                                        authTrigger++
                                        navigateWithLoading(Screen.ParentDashboard)
                                    },
                                    onCreateAccountClick = { currentScreen = Screen.CreateParentAccount },
                                    onBackClick = { currentScreen = Screen.Entry }
                                )
                            }
                            is Screen.CreateParentAccount -> {
                                val isLoading by loginViewModel.isLoading.collectAsState()
                                CreateParentAccountScreen(
                                    onCreateClick = { parentName, email, password, kidNames, selectedAgeRange, selectedGender ->
                                        loginViewModel.signup(email, password, parentName, kidNames, selectedAgeRange, selectedGender)
                                    },
                                    onBackClick = { currentScreen = Screen.Login },
                                    onGoogleSignInClick = {
                                        googleLauncher.launch(googleSignInClient.signInIntent)
                                    },
                                    isLoading = isLoading
                                )

                                val loginState by loginViewModel.loginState.collectAsState()
                                LaunchedEffect(loginState) {
                                    if (loginState is LoginViewModel.LoginState.Success) {
                                        navigateWithLoading(Screen.ParentDashboard)
                                    }
                                }
                            }
                            is Screen.ParentDashboard -> {
                                authTrigger++
                                ParentDashboardScreen(
                                    onAddKidClick = { currentScreen = Screen.AddKid },
                                    onBackClick = {
                                        authTrigger++
                                        currentScreen = Screen.Entry
                                    },
                                    onLogoutClick = {
                                        scope.launch {
                                            allKids = emptyList()
                                            selectedKid = null
                                            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                                            googleSignInClient.signOut()
                                            authTrigger++
                                            navigateWithLoading(Screen.Entry)
                                        }
                                    }
                                )
                            }
                            is Screen.AddKid -> {
                                val dashboardViewModel: ParentDashboardViewModel = viewModel()
                                AddKidScreen(
                                    onAddClick = { name, gender, age ->
                                        dashboardViewModel.addKid(name, gender, age)
                                        authTrigger++
                                        navigateWithLoading(Screen.ParentDashboard)
                                    },
                                    onBackClick = { currentScreen = Screen.ParentDashboard },
                                    isLoading = false
                                )
                            }
                            is Screen.KidHome -> {
                                KidHomeScreen(
                                    onRoutineClick = { navigateWithLoading(Screen.RoutineSelection) },
                                    onRewardsClick = { navigateWithLoading(Screen.Rewards) },
                                    onBackClick = { navigateWithLoading(Screen.Entry) }
                                )
                            }
                            is Screen.RoutineSelection -> {
                                RoutineSelectionScreen(
                                    kid = selectedKid,
                                    onRoutineSelected = { routineId ->
                                        currentScreen = Screen.Routine(routineId)
                                    },
                                    onBackClick = { navigateWithLoading(Screen.KidHome) }
                                )
                            }
                            is Screen.Routine -> {
                                RoutineScreen(
                                    routineId = screen.routineId,
                                    globalTimeLeft = timeLeftGlobal,
                                    onRoutineComplete = { starsEarned ->
                                        scope.launch {
                                            if (selectedKid != null) {
                                                parentRepository.addStars(selectedKid!!.id, starsEarned)
                                                when (screen.routineId) {
                                                    "Morning Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "brushing")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "morning", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Bedtime Routine")
                                                    }
                                                    "Bedtime Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "bedtime")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "bedtime", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Mealtime Routine")
                                                    }
                                                    "Mealtime Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "meal")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "meal", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "School Routine")
                                                    }
                                                    "School Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "school")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "school", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Transportation Routine")
                                                    }
                                                    "Transportation Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "transport")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "transport", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Animals Routine")
                                                    }
                                                    "Animals Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "animals")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "animals", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Birds Routine")
                                                    }
                                                    "Birds Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "birds")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "birds", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Shapes Routine")
                                                    }
                                                    "Shapes Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "shapes")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "shapes", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Hospital Routine")
                                                    }
                                                    "Hospital Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "hospital")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "hospital", starsEarned)
                                                        parentRepository.unlockRoutine(selectedKid!!.id, "Marriage Routine")
                                                    }
                                                    "Marriage Routine" -> {
                                                        parentRepository.completeTask(selectedKid!!.id, "marriage")
                                                        parentRepository.updateRoutineScore(selectedKid!!.id, "marriage", starsEarned)
                                                    }
                                                }
                                            }
                                            navigateWithLoading(Screen.MiniGame)
                                        }
                                    }
                                )
                            }
                            is Screen.MiniGame -> {
                                MiniGameScreen(
                                    onGameFinished = { extraStars ->
                                        scope.launch {
                                            if (extraStars > 0 && selectedKid != null) {
                                                parentRepository.addStars(selectedKid!!.id, extraStars)
                                            }
                                            navigateWithLoading(Screen.EndRoutine)
                                        }
                                    }
                                )
                            }
                            is Screen.EndRoutine -> {
                                EndRoutineScreen(
                                    onHomeClick = { navigateWithLoading(Screen.RoutineSelection) }
                                )
                            }
                            is Screen.Rewards -> {
                                RewardsScreen(
                                    totalStars = selectedKid?.totalStars ?: 0,
                                    onBackClick = { navigateWithLoading(Screen.KidHome) }
                                )
                            }
                        }

                        if (showGuestLoginDialog) {
                            AlertDialog(
                                onDismissRequest = { showGuestLoginDialog = false },
                                title = { Text("Not Logged In!", fontWeight = FontWeight.Bold, color = Color.Red) },
                                text = {
                                    Text("Your progress (Stars & Routines) will NOT be saved if you delete the app.\n\nPlease login to sync your data!", color = Color.Black)
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            showGuestLoginDialog = false
                                            navigateWithLoading(Screen.Login)
                                        }
                                    ) {
                                        Text("Login Now")
                                    }
                                },
                                dismissButton = {
                                    androidx.compose.material3.TextButton(
                                        onClick = {
                                            showGuestLoginDialog = false

                                            ScreenTimeManager.startTimer()
                                            navigateWithLoading(Screen.KidHome)
                                        }
                                    ) {
                                        Text("Continue as Guest", color = Color.Gray)
                                    }
                                },
                                containerColor = Color.White
                            )
                        }

                        if (showWarning) {
                            AlertDialog(
                                onDismissRequest = { ScreenTimeManager.dismissWarning() },
                                title = { Text("Time Check!", fontWeight = FontWeight.Bold) },
                                text = { Text("Hi! Toony here. You have 5 minutes left to play!") },
                                confirmButton = {
                                    Button(onClick = { ScreenTimeManager.dismissWarning() }) {
                                        Text("OK")
                                    }
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.toony_happy),
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                            )
                        }

                        AnimatedVisibility(
                            visible = showLoadingOverlay,
                            enter = expandHorizontally(
                                expandFrom = Alignment.Start,
                                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                            ),
                            exit = shrinkHorizontally(
                                shrinkTowards = Alignment.End,
                                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing))
                        ) {
                            ThinkingScreenOverlay()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ScreenTimeManager.checkLockStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        ScreenTimeManager.stopTimer()
    }
}

@Composable
fun ByeByeScreen() {
    val context = LocalContext.current
    var countdown by remember { mutableIntStateOf(5) }


    androidx.activity.compose.BackHandler(enabled = true) { }


    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000L)
            countdown--
        }
        val activity = context as? Activity
        activity?.finish()
    }


    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A237E)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(R.drawable.toony_bye)
                        .build(),
                    imageLoader = imageLoader
                ),
                contentDescription = "Toony Waving",
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Bye Bye!",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text="See You Tomorrow!",
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Closing in $countdown...",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}