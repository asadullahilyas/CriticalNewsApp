package com.asadullah.criticalnewsapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.common.Settings
import com.asadullah.criticalnewsapp.common.mutableStateIn
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import com.asadullah.criticalnewsapp.features.home.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val settings: Settings
) : ViewModel() {

    private var currentPage = 1

    private var isLoadingData = false

    private val _uiEventsFlow: MutableSharedFlow<UIEvent> = MutableSharedFlow()
    val uiEventsFlow = _uiEventsFlow.asSharedFlow()

    private val _isUserAuthenticated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUserAuthenticated = _isUserAuthenticated.asStateFlow()

    private val _biometricAuthEnabled: MutableStateFlow<Boolean> =
        settings.getBiometricAuthEnabled().mutableStateIn(viewModelScope, false)
    val biometricAuthEnabled = _biometricAuthEnabled.asStateFlow()

    private val _topHeadlines: MutableStateFlow<List<Article>> =
        MutableStateFlow(emptyList())
    val topHeadlines = _topHeadlines.asStateFlow()

    private val _showMainCircularIndicator: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showMainCircularIndicator = _showMainCircularIndicator.asStateFlow()

    private val _errorResponse: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorResponse = _errorResponse.asStateFlow()

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            UserEvent.DoNothing -> {}
            UserEvent.InitiateUserAuthentication -> initiateUserAuthentication()
            UserEvent.LoadNextPage -> fetchTopHeadlines(currentPage)
            UserEvent.ShowBiometricPrompt -> showBiometricPrompt()
            UserEvent.ToggleBiometricAuth -> toggleBiometricAuth()
            UserEvent.AppAccessGranted -> appAccessGranted()
            UserEvent.AppAccessDenied -> appAccessDenied()
        }
    }

    private fun initiateUserAuthentication() {
        viewModelScope.launch {

            val isBiometricAuthenticationEnabled = settings.getBiometricAuthEnabled().first()
            if (isBiometricAuthenticationEnabled) {
                _uiEventsFlow.emit(
                    UIEvent.AuthenticateUser(
                        title = "Authorization",
                        description = "Please authorize yourself in order to access the app."
                    )
                )
            } else {
                appAccessGranted()
            }
        }
    }

    private fun fetchTopHeadlines(pageNumber: Int) {

        if (isLoadingData) return

        isLoadingData = true

        viewModelScope.launch {
            getTopHeadlinesUseCase(pageNumber).collect { response ->

                isLoadingData = false

                when (response) {
                    is Response.Loading -> {
                        _showMainCircularIndicator.value = _topHeadlines.value.isEmpty()
                        _errorResponse.value = null
                    }

                    is Response.Error -> {
                        _showMainCircularIndicator.value = false
                        _errorResponse.value = response.message ?: "Unknown error occurred"
                        _topHeadlines.value = emptyList()
                        _uiEventsFlow.emit(UIEvent.ApiResponseFailed)
                    }

                    is Response.Success -> {
                        currentPage++

                        println("currentPage new value: $currentPage")

                        _showMainCircularIndicator.value = false
                        _errorResponse.value = null

                        val topHeadlinesMutableList = _topHeadlines.value.toMutableList()

                        topHeadlinesMutableList.addAll(
                            response
                                .data
                                ?.articles
                                ?.sortedByDescending { it.publishedAt } ?: emptyList()
                        )

                        _topHeadlines.value = topHeadlinesMutableList
                    }
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        viewModelScope.launch {
            _uiEventsFlow.emit(
                UIEvent.ShowBiometricPromptForEnablingBiometricAuth(
                    title = "Authorization",
                    description = "Please authorize if you want to ${if (biometricAuthEnabled.value) "disable" else "enable"} Biometric Authentication."
                )
            )
        }
    }

    private fun toggleBiometricAuth() {
        viewModelScope.launch {
            settings.setBiometricAuthEnabled(enabled = biometricAuthEnabled.value.not())
        }
    }

    private fun appAccessGranted() {
        _errorResponse.value = null
        _isUserAuthenticated.value = true
    }

    private fun appAccessDenied() {
        _errorResponse.value = "You are not authorized to access this app."
        viewModelScope.launch {
            _uiEventsFlow.emit(UIEvent.BiometricAuthenticationFailed)
        }
    }
}

sealed interface UserEvent {
    object InitiateUserAuthentication : UserEvent
    object LoadNextPage : UserEvent
    object ShowBiometricPrompt : UserEvent
    object ToggleBiometricAuth : UserEvent
    object AppAccessGranted : UserEvent
    object AppAccessDenied : UserEvent
    object DoNothing : UserEvent
}

sealed interface UIEvent {
    data class ShowBiometricPromptForEnablingBiometricAuth(val title: String, val description: String) : UIEvent
    data class AuthenticateUser(val title: String, val description: String) : UIEvent
    object ApiResponseFailed: UIEvent
    object BiometricAuthenticationFailed : UIEvent
}