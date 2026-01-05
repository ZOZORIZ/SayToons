package com.saytoons.app

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

object ScreenTimeManager {



    private const val SESSION_DURATION_MS = 15 * 60 * 1000L


    private const val WARNING_TRIGGER_REMAINING_MS = 5 * 60 * 1000L


    private const val LOCKOUT_DURATION_MS = 15 * 60 * 1000L

    private const val PREFS_NAME = "saytoons_prefs"
    private const val KEY_UNLOCK_TIME = "unlock_timestamp"

    private var timer: CountDownTimer? = null
    private var warningShown = false
    private var lockoutTimer: CountDownTimer? = null
    private var prefs: SharedPreferences? = null


    private val _timeLeftFormatted = MutableStateFlow("15:00")
    val timeLeftFormatted = _timeLeftFormatted.asStateFlow()

    private val _showWarning = MutableStateFlow(false)
    val showWarning = _showWarning.asStateFlow()

    private val _shouldCloseApp = MutableStateFlow(false)
    val shouldCloseApp = _shouldCloseApp.asStateFlow()

    private val _isKidModeLocked = MutableStateFlow(false)
    val isKidModeLocked = _isKidModeLocked.asStateFlow()

    private val _lockTimeLeftFormatted = MutableStateFlow("")
    val lockTimeLeftFormatted = _lockTimeLeftFormatted.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        checkLockStatus()
    }

    fun checkLockStatus() {
        val unlockTime = prefs?.getLong(KEY_UNLOCK_TIME, 0L) ?: 0L
        val currentTime = System.currentTimeMillis()

        if (unlockTime > currentTime) {
            _isKidModeLocked.value = true
            _shouldCloseApp.value = true
            startLockoutCountdown(unlockTime - currentTime)
        } else {
            _isKidModeLocked.value = false
            lockoutTimer?.cancel()
        }
    }

    fun startTimer() {

        if (_isKidModeLocked.value) return

        stopTimer()
        warningShown = false
        _showWarning.value = false
        _shouldCloseApp.value = false

        timer = object : CountDownTimer(SESSION_DURATION_MS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateFormattedTime(millisUntilFinished)


                if (millisUntilFinished <= WARNING_TRIGGER_REMAINING_MS && !warningShown) {
                    _showWarning.value = true
                    warningShown = true
                }
            }

            override fun onFinish() {
                _timeLeftFormatted.value = "00:00"
                lockKidMode()
                _shouldCloseApp.value = true
            }
        }.start()
    }

    private fun lockKidMode() {
        val unlockTime = System.currentTimeMillis() + LOCKOUT_DURATION_MS
        prefs?.edit()?.putLong(KEY_UNLOCK_TIME, unlockTime)?.apply()
        checkLockStatus()
    }

    private fun startLockoutCountdown(durationMs: Long) {
        lockoutTimer?.cancel()
        lockoutTimer = object : CountDownTimer(durationMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                _lockTimeLeftFormatted.value = String.format("%02dm %02ds", minutes, seconds)
            }

            override fun onFinish() {
                _isKidModeLocked.value = false
                _lockTimeLeftFormatted.value = ""
                _shouldCloseApp.value = false // Unlock the view
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    fun dismissWarning() {
        _showWarning.value = false
    }

    private fun updateFormattedTime(millis: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        _timeLeftFormatted.value = String.format("%02d:%02d", minutes, seconds)
    }
}