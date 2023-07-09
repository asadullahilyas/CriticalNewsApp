package com.asadullah.criticalnewsapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.common.Settings
import com.asadullah.criticalnewsapp.common.mutableStateIn
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import com.asadullah.criticalnewsapp.features.home.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val settings: Settings
) : ViewModel() {

    private var currentPage = 1

    private var isLoadingData = false

    private val _biometricPromptState: MutableStateFlow<BiometricUIData> = MutableStateFlow(
        BiometricUIData()
    )
    val biometricPromptState = _biometricPromptState.asStateFlow()

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

    private val _errorResponse: MutableStateFlow<ErrorUIData?> = MutableStateFlow(null)
    val errorResponse = _errorResponse.asStateFlow()

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            UserEvent.DoNothing -> dismissBiometricPrompt()
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
                _biometricPromptState.update {
                    it.copy(
                        isShowing = true,
                        title = "Authorization",
                        description = "Please authorize yourself in order to access the app.",
                        onAuthSuccessAction = UserEvent.AppAccessGranted,
                        onAuthFailAction = UserEvent.AppAccessDenied,
                        onAuthCancelAction = UserEvent.AppAccessDenied
                    )
                }
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
                        _topHeadlines.value = emptyList()
                        _errorResponse.value = ErrorUIData(
                            errorMessage = response.message ?: "Unknown error occurred",
                            onRetry = UserEvent.LoadNextPage
                        )
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
            _biometricPromptState.update {
                it.copy(
                    isShowing = true,
                    title = "Authorization",
                    description = "Please authorize if you want to ${if (biometricAuthEnabled.value) "disable" else "enable"} Biometric Authentication.",
                    onAuthSuccessAction = UserEvent.ToggleBiometricAuth
                )
            }
        }
    }

    private fun toggleBiometricAuth() {
        _errorResponse.value = null
        _biometricPromptState.value = BiometricUIData()
        viewModelScope.launch {
            settings.setBiometricAuthEnabled(enabled = biometricAuthEnabled.value.not())
        }
    }

    private fun appAccessGranted() {
        _errorResponse.value = null
        _isUserAuthenticated.value = true
        dismissBiometricPrompt()
    }

    private fun appAccessDenied() {
        _errorResponse.value = ErrorUIData(
            errorMessage = "You are not authorized to access this app.",
            onRetry = UserEvent.InitiateUserAuthentication
        )
        dismissBiometricPrompt()
    }

    private fun dismissBiometricPrompt() {
        _biometricPromptState.value = BiometricUIData()
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

data class BiometricUIData(
    val isShowing: Boolean = false,
    val title: String = "",
    val description: String = "",
    val onAuthSuccessAction: UserEvent = UserEvent.DoNothing,
    val onAuthFailAction: UserEvent = UserEvent.DoNothing,
    val onAuthCancelAction: UserEvent = UserEvent.DoNothing
)

data class ErrorUIData(
    val errorMessage: String = "",
    val onRetry: UserEvent = UserEvent.DoNothing
)