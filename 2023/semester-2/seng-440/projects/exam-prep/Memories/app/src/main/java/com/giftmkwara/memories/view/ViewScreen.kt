package com.giftmkwara.memories.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.giftmkwara.memories.shared.MainScaffold
import com.giftmkwara.memories.shared.MainViewModel
import com.giftmkwara.memories.shared.formatDate
import com.giftmkwara.memories.shared.formatTime
import com.giftmkwara.memories.shared.isPortrait

@Composable
fun ViewScreen(mainViewModel: MainViewModel) {
    val count = if (isPortrait()) 1 else 2

    MainScaffold {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                text = "Hi ${mainViewModel.username} 👋🏿"
            )
            Spacer(modifier = Modifier.size(32.dp))
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(count = count)
            ) {
                items(mainViewModel.memories) {
                    Card(modifier = Modifier.padding(8.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            Text(
                                text = it.description,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${formatDate(it.date)}, ${formatTime(it.time)}",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}