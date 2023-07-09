@file:OptIn(ExperimentalMaterial3Api::class)

package com.asadullah.criticalnewsapp.features.home.presentation

import androidx.biometric.BiometricManager
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asadullah.criticalnewsapp.BuildConfig
import com.asadullah.criticalnewsapp.R
import com.asadullah.criticalnewsapp.common.isNeitherNullNorEmptyNorBlank
import com.asadullah.criticalnewsapp.features.biometric.presentation.BiometricPrompt
import com.asadullah.criticalnewsapp.features.destinations.ArticleDetailsScreenDestination
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {

    val viewModel: HomeScreenViewModel = hiltViewModel()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {

            val context = LocalContext.current
            val isBiometricAuthAvailable = BiometricManager
                .from(context)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

            val biometricAuthEnabled by viewModel.biometricAuthEnabled.collectAsStateWithLifecycle()

            TopAppBar(
                title = {
                    Text(text = BuildConfig.SOURCE_NAME)
                },
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    if (isBiometricAuthAvailable) {
                        IconToggleButton(
                            checked = biometricAuthEnabled,
                            onCheckedChange = {
                                viewModel.onUserEvent(UserEvent.ShowBiometricPrompt)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                tint = if (biometricAuthEnabled) Color.Green else Color.DarkGray,
                                contentDescription = "Touch ID"
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                navigator.navigate(ArticleDetailsScreenDestination(article = it))
            }
        }
    )
}

@Composable
fun MainContent(modifier: Modifier = Modifier, onArticleClicked: (article: Article) -> Unit) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.onUserEvent(UserEvent.InitiateUserAuthentication)
    }

    val isUserAuthenticated by viewModel.isUserAuthenticated.collectAsStateWithLifecycle()

    val topHeadlines by viewModel.topHeadlines.collectAsStateWithLifecycle()
    val showMainCircularIndicator by viewModel.showMainCircularIndicator.collectAsStateWithLifecycle()
    val errorResponse by viewModel.errorResponse.collectAsStateWithLifecycle()

    val biometricPromptState by viewModel.biometricPromptState.collectAsStateWithLifecycle()

    var actionOnRetry by remember<MutableState<UserEvent>> {
        mutableStateOf(UserEvent.LoadNextPage)
    }

    if (biometricPromptState.isShowing) {
        BiometricPrompt(
            title = biometricPromptState.title,
            description = biometricPromptState.description,
            negativeButton = "Cancel",
            onAuthenticated = {
                viewModel.onUserEvent(biometricPromptState.onAuthSuccessAction)
            },
            onCancel = {
                viewModel.onUserEvent(biometricPromptState.onAuthCancelAction)
            },
            onAuthenticationError = {
                viewModel.onUserEvent(biometricPromptState.onAuthFailAction)
            },
            onAuthenticationSoftError = {
                viewModel.onUserEvent(biometricPromptState.onAuthFailAction)
            }
        )
    }

    Box(
        modifier = modifier.then(
            Modifier
                .background(Color(0xFFF1F1F1))
        )
    ) {
        if (showMainCircularIndicator) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (errorResponse != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = errorResponse!!.errorMessage,
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = Color.Red.copy(alpha = 0.9f))
                )
                TextButton(onClick = {
                    viewModel.onUserEvent(errorResponse!!.onRetry)
                }) {
                    Text("Retry")
                }
            }
        } else if (isUserAuthenticated) {

            val lazyListState = rememberLazyListState()

            lazyListState.OnBottomReached {
                viewModel.onUserEvent(UserEvent.LoadNextPage)
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {

                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }

                items(count = topHeadlines.size) { index ->
                    val article = topHeadlines[index]
                    ArticleView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        article = article,
                        onArticleClicked = onArticleClicked
                    )
                    if (index == topHeadlines.lastIndex) {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ArticleView(
    article: Article,
    modifier: Modifier = Modifier,
    onArticleClicked: (article: Article) -> Unit
) {
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
                .padding(all = 4.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
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
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = article.author ?: "Anonymous", style = TextStyle(color = Color.Gray))
            Text(text = article.publishedTimeAgo() ?: "N/A", style = TextStyle(color = Color.Gray))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = article.title ?: "Unknown title",
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.End),
            onClick = { onArticleClicked(article) }) {
            Text(text = "See full story")
        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            loadMore()
        }
    }
}