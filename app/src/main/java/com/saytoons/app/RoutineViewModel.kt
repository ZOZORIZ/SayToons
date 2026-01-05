package com.saytoons.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import com.saytoons.app.data.RoutineRepository
import com.saytoons.app.data.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.net.URLEncoder
import com.saytoons.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import android.graphics.BitmapFactory


class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    enum class UiState {
        INSTRUCTION, LISTENING, GENERATING, SUCCESS, COMPLETED, LOADING
    }

    private val _uiState = MutableStateFlow(UiState.INSTRUCTION)
    val uiState = _uiState.asStateFlow()

    private val _spokenText = MutableStateFlow("")
    val spokenText = _spokenText.asStateFlow()

    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask = _currentTask.asStateFlow()

    private val _displayImage = MutableStateFlow<Any?>(null)
    val displayImage = _displayImage.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private val _starsCollected = MutableStateFlow(0)
    val starsCollected = _starsCollected.asStateFlow()

    private var taskList: List<Task> = emptyList()
    private var currentIndex = 0
    private var currentRoutineId: String? = null
    private var taskStartTime: Long = 0

    init {
        voiceToTextParser.state.onEach { state ->
            _spokenText.value = state.spokenText


            if (!state.isSpeaking && _uiState.value == UiState.LISTENING) {
                _uiState.value = UiState.INSTRUCTION
            }

            if (state.spokenText.isNotBlank()) {
                onSpeechResult(state.spokenText)
            }
        }.launchIn(viewModelScope)
    }

    fun loadRoutine(routineId: String) {
        currentRoutineId = routineId
        taskList = RoutineRepository.getRoutine(routineId)
        currentIndex = 0
        _starsCollected.value = 0
        loadCurrentTask()
    }

    private fun loadCurrentTask() {
        if (currentIndex < taskList.size) {
            val task = taskList[currentIndex]
            _currentTask.value = task


            taskStartTime = System.currentTimeMillis()


            _displayImage.value = null

            _uiState.value = UiState.INSTRUCTION
            _spokenText.value = ""
            _progress.value = (currentIndex.toFloat()) / taskList.size.toFloat()

            if (currentIndex + 1 < taskList.size) {
                val nextTask = taskList[currentIndex + 1]
                val request = ImageRequest.Builder(application)
                    .data(nextTask.initialImageRes)
                    .build()
                coil.Coil.imageLoader(application).enqueue(request)
            }

        } else {

            _uiState.value = UiState.COMPLETED
        }
    }

    fun handleMicClick() {
        if (_uiState.value == UiState.LISTENING) {
            voiceToTextParser.stopListening()
            _uiState.value = UiState.INSTRUCTION
        } else {
            voiceToTextParser.startListening()
            _uiState.value = UiState.LISTENING
        }
    }

    private fun onSpeechResult(text: String) {
        val task = _currentTask.value ?: return
        val spoken = text.lowercase()


        val title = task.title.lowercase()
        val command = task.expectedSpeech.lowercase()


        val isGenericDone = spoken.contains("done") ||
                spoken.contains("finish") ||
                spoken.contains("next") ||
                spoken.contains("yes") ||
                spoken.contains("okay")


        val ratioTitle = FuzzySearch.weightedRatio(spoken, title)
        val ratioCommand = FuzzySearch.weightedRatio(spoken, command)


        val bestScore = maxOf(ratioTitle, ratioCommand)


        val ignoredWords = listOf("the", "a", "an", "to", "my", "your", "try", "saying", "i", "am")
        val allTargetWords = (title.split(" ") + command.split(" "))
            .filter { !ignoredWords.contains(it) && it.length > 2 }

        val hasKeyWord = allTargetWords.any { spoken.contains(it) }


        if (bestScore > 50 || hasKeyWord || isGenericDone) {

            android.util.Log.d("SayToonsSpeech", "MATCH! Spoken: '$spoken'. Score: $bestScore")

            _uiState.value = UiState.GENERATING
            voiceToTextParser.stopListening()
            generateRewardImage()
        } else {
            android.util.Log.d("SayToonsSpeech", "NO MATCH. Spoken: '$spoken'. Best Score: $bestScore")
        }
    }

    private fun generateRewardImage() {
        val task = _currentTask.value ?: return
        val apiKey = try { BuildConfig.POLLINATIONS_API_KEY } catch (e: Exception) { "" }

        viewModelScope.launch(Dispatchers.IO) {


            val promptText = if (task.aiPrompt.isNotBlank()) task.aiPrompt else "Cartoon illustration of ${task.title}"
            val encodedPrompt = URLEncoder.encode(promptText, "UTF-8")


            val urlString = "https://gen.pollinations.ai/image/$encodedPrompt?model=turbo&nologo=true&width=512&height=512"

            android.util.Log.d("SayToonsGen", "Downloading fast version: $urlString")

            try {
                val connection = java.net.URL(urlString).openConnection() as java.net.HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $apiKey")
                connection.connectTimeout = 10000

                if (connection.responseCode == 200) {
                    val inputStream = connection.inputStream
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    _displayImage.value = bitmap

                } else {
                    _displayImage.value = task.initialImageRes
                }
                connection.disconnect()

            } catch (e: Exception) {
                e.printStackTrace()
                _displayImage.value = task.initialImageRes
            }

            _uiState.value = UiState.SUCCESS
        }
    }

    fun onNextClicked() {

        val currentTime = System.currentTimeMillis()
        val timeTakenMs = currentTime - taskStartTime


        val starsEarned = if (timeTakenMs < 20000) {
            2
        } else {
            1
        }

        _starsCollected.value += starsEarned

        currentIndex++
        loadCurrentTask()
    }

    override fun onCleared() {
        super.onCleared()
        voiceToTextParser.stopListening()
    }
}