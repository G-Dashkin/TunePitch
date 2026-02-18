package com.dashkin.tunepitch.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkin.tunepitch.feature.home.domain.usecase.GetBestResultsUseCase
import com.dashkin.tunepitch.feature.home.domain.usecase.GetExercisesUseCase
import com.dashkin.tunepitch.feature.home.presentation.state.HomeEvent
import com.dashkin.tunepitch.feature.home.presentation.state.HomeSideEffect
import com.dashkin.tunepitch.feature.home.presentation.state.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getExercises: GetExercisesUseCase,
    private val getBestResults: GetBestResultsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadData()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnExerciseClick -> {
                viewModelScope.launch {
                    _sideEffect.send(HomeSideEffect.NavigateToExercise(event.exerciseId))
                }
            }
            is HomeEvent.OnTunerClick -> {
                viewModelScope.launch {
                    _sideEffect.send(HomeSideEffect.NavigateToTuner)
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val exercises = getExercises()
            val bestResults = getBestResults()
            _state.update {
                it.copy(
                    exercises = exercises,
                    bestResults = bestResults,
                    isLoading = false
                )
            }
        }
    }
}
