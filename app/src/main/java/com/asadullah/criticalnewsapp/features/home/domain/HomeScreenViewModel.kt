package com.asadullah.criticalnewsapp.features.home.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _topHeadlines: MutableStateFlow<TopHeadlines> = MutableStateFlow(TopHeadlines())
    val topHeadlines = _topHeadlines.asStateFlow()

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            UserEvent.InitialLoad -> fetchTopHeadlines(1)
            is UserEvent.PageLoad -> fetchTopHeadlines(userEvent.pageNumber)
        }
    }

    private fun fetchTopHeadlines(pageNumber: Int) {
        viewModelScope.launch {
            getTopHeadlinesUseCase(pageNumber).collect {
                _topHeadlines.value = it.data ?: TopHeadlines()
            }
        }
    }
}

sealed interface UserEvent {
    object InitialLoad : UserEvent
    data class PageLoad(val pageNumber: Int) : UserEvent
}