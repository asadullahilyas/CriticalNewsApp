package com.asadullah.criticalnewsapp.features.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Weeks
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private var currentPage = 1

    private var isLoadingData = false

    private val _topHeadlines: MutableStateFlow<List<Article>> =
        MutableStateFlow(emptyList())
    val topHeadlines = _topHeadlines.asStateFlow()

    private val _showMainCircularIndicator: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showMainCircularIndicator = _showMainCircularIndicator.asStateFlow()

    private val _errorResponse: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorResponse = _errorResponse.asStateFlow()

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            UserEvent.LoadNextPage -> fetchTopHeadlines(currentPage)
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
}

sealed interface UserEvent {
    object LoadNextPage : UserEvent
}