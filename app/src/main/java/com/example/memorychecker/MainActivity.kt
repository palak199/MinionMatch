package com.example.memorychecker

import MemoryCardLayout
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memorychecker.ui.theme.MemoryCheckerTheme
import com.example.memorychecker.viewmodel.MemoryCardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MemoryCheckerTheme {
                val viewModel: MemoryCardViewModel = viewModel()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .paint(
                            painterResource(id = R.drawable.bg),
                            contentScale = ContentScale.FillBounds
                        )
                ) {
                    Row {
                        Text(
                            text = "Score: ${viewModel.score.value}",
                            fontSize = 24.sp,
                            color = Color(0xFFe91f64),
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        OutlinedButton(
                            onClick = { viewModel.resetGame() },
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.Black),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = "refresh")
                        }
                    }

                    MemoryCardLayout(memoryCards = viewModel.memoryCards,
                        onCardUpdate = { card ->
                            viewModel.onCardClicked(card)
                        })
                }
            }
        }
    }
}
