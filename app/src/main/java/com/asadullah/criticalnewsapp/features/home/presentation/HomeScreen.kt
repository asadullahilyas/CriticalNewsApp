@file:OptIn(ExperimentalMaterial3Api::class)

package com.asadullah.criticalnewsapp.features.home.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asadullah.criticalnewsapp.BuildConfig
import com.asadullah.criticalnewsapp.features.home.domain.HomeScreenViewModel
import com.asadullah.criticalnewsapp.features.home.domain.UserEvent

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = BuildConfig.SOURCE_NAME) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            MainContent(
                modifier = Modifier.fillMaxSize(),
                paddingValues = paddingValues
            )
        }
    )
}

@Composable
fun MainContent(modifier: Modifier = Modifier, paddingValues: PaddingValues) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.onUserEvent(UserEvent.InitialLoad)
    }

    val response = viewModel.topHeadlines.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier,
    ) {
        items(count = response.value.articles.size) { index ->
            val article = response.value.articles[index]
            Text(article.title ?: "N/A")
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeScreen()
}