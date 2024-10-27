package com.example.memorychecker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.memorychecker.R
import com.example.memorychecker.model.MemoryCard

@Composable
fun MemoryCardView(
    card: MemoryCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .width(36.dp)
            .padding(6.dp)
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
    ) {
        Image(
            painter = painterResource(id = if (card.flipped.value) card.imgResId else R.drawable.question),
            contentDescription = "Memory Card",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}