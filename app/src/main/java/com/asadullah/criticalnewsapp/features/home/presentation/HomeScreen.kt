@file:OptIn(ExperimentalMaterial3Api::class)

package com.asadullah.criticalnewsapp.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asadullah.criticalnewsapp.R
import com.asadullah.criticalnewsapp.BuildConfig
import com.asadullah.criticalnewsapp.common.isNeitherNullNorEmptyNorBlank
import com.asadullah.criticalnewsapp.features.home.domain.HomeScreenViewModel
import com.asadullah.criticalnewsapp.features.home.domain.UserEvent
import com.asadullah.criticalnewsapp.features.home.domain.model.Article

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = BuildConfig.SOURCE_NAME) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        }
    )
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.onUserEvent(UserEvent.LoadNextPage)
    }

    val topHeadlines by viewModel.topHeadlines.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorResponse by viewModel.errorResponse.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.then(
            Modifier
                .background(Color(0xFFF1F1F1))
        )
    ) {

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (errorResponse.isNeitherNullNorEmptyNorBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = errorResponse!!,
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = Color.Red.copy(alpha = 0.9f))
                )
                TextButton(onClick = {
                    viewModel.onUserEvent(UserEvent.LoadNextPage)
                }) {
                    Text("Retry")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(count = topHeadlines.articles.size) { index ->
                    val article = topHeadlines.articles[index]
                    ArticleView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 30.dp),
                        article = article
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleView(article: Article, modifier: Modifier = Modifier) {
    Column {
        Column(
            modifier = modifier
                .then(
                    Modifier
                        .clip(RoundedCornerShape(size = 10.dp))
                        .background(Color.White)
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 4.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(size = 10.dp)),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.urlToImage)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    fallback = painterResource(R.drawable.placeholder),
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = article.author ?: "Anonymous", style = TextStyle(color = Color.Gray))
                Text(text = article.publishedAt ?: "N/A", style = TextStyle(color = Color.Gray))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeScreen()
}