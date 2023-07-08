@file:OptIn(ExperimentalMaterial3Api::class)

package com.asadullah.criticalnewsapp.features.detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asadullah.criticalnewsapp.R
import com.asadullah.criticalnewsapp.common.format
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ArticleDetailsScreen(
    navigator: DestinationsNavigator,
    article: Article
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = article.source?.name ?: "N/A")
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(10.dp)),
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

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Published at: ${article.publishedAt?.format("hh:mm a, EEE, MMM dd, YYYY") ?: "N/A"}",
                style = TextStyle(color = Color.Gray)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = article.title ?: "Unknown title",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "\"${article.description ?: "Description unavailable"}\"",
                style = TextStyle(
                    color = Color(0XFF838383),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = article.content ?: "Content unavailable",
                style = TextStyle(
                    color = Color(0xFF242424),
                    fontSize = 14.sp
                )
            )
        }
    }
}