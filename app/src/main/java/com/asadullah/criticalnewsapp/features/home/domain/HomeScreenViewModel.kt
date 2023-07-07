package com.asadullah.criticalnewsapp.features.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private var currentPage = 0

    private val _topHeadlines: MutableStateFlow<TopHeadlines> = MutableStateFlow(TopHeadlines())
    val topHeadlines = _topHeadlines.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorResponse: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorResponse = _errorResponse.asStateFlow()

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            UserEvent.LoadNextPage -> fetchTopHeadlines(++currentPage)
        }
    }

    private fun fetchTopHeadlines(pageNumber: Int) {
        viewModelScope.launch {
            getTopHeadlinesUseCase(pageNumber).collect { response ->

                when (response) {
                    is Response.Loading -> {
                        _isLoading.value = true
                        _errorResponse.value = null
                        _topHeadlines.value = TopHeadlines()
                    }
                    is Response.Error -> {
                        _isLoading.value = false
                        _errorResponse.value = response.message ?: "Unknown error occurred"
                        _topHeadlines.value = TopHeadlines()
                    }
                    is Response.Success -> {
                        _isLoading.value = false
                        _errorResponse.value = null
                        _topHeadlines.value = response.data ?: TopHeadlines()
                    }
                }
            }
        }
    }
}

sealed interface UserEvent {
    object LoadNextPage : UserEvent
}