package com.saytoons.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saytoons.app.data.Kid
import com.saytoons.app.data.ParentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParentDashboardViewModel : ViewModel() {

    private val repository = ParentRepository()

    private val _kids = MutableStateFlow<List<Kid>>(emptyList())
    val kids = _kids.asStateFlow()

    init {
        loadKids()
    }

    private fun loadKids() {
        viewModelScope.launch {
            repository.getKidsFlow()?.collect { kidsList ->
                _kids.value = kidsList
            }
        }
    }


    fun addKid(name: String, gender: String, ageRange: String) {
        viewModelScope.launch {
            repository.addKid(name, gender, ageRange)
        }
    }
}