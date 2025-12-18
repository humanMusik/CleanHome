package com.humanmusik.cleanhome.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.domain.model.Home
import com.humanmusik.cleanhome.presentation.onLoading
import com.humanmusik.cleanhome.presentation.onSuccess

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    HomeContent(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Home") })
        },
    ) { paddingValues ->
        state.homes
            .onLoading {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            .onSuccess { homes ->
                HomeSuccessContent(
                    scaffoldPadding = paddingValues,
                    homes = homes,
                )
            }
    }
}

@Composable
private fun HomeSuccessContent(
    scaffoldPadding: PaddingValues,
    homes: List<Home>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
    ) {
        items(
            items = homes,
        ) { home ->
            HomeCard(
                home = home,
                onClick = { }
            )
        }
    }
}

@Composable
fun HomeCard(
    home: Home,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer, // Custom background color
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer // Custom content (text/icon) color
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(home.name)
        }
    }
}