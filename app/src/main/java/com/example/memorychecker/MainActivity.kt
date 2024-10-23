package com.example.memorychecker

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
import com.example.memorychecker.ui.theme.MemoryCheckerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MemoryCheckerTheme {
                var score by rememberSaveable { mutableStateOf(0) }
                val cardImages = listOf(
                    R.drawable.minion_1,
                    R.drawable.minion_2,
                    R.drawable.minion_3,
                    R.drawable.minion_4,
                    R.drawable.minion_5,
                    R.drawable.minion_6,
                )
                var memoryCards by rememberSaveable { mutableStateOf(createCards(cardImages)) }
                var flippedCards by rememberSaveable { mutableStateOf<List<MemoryCard>>(emptyList()) }
                var matchedCards by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }

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
                            text = "Score: $score",
                            fontSize = 24.sp,
                            color = Color(0xFFe91f64),
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        OutlinedButton(
                            onClick = {
                                score = 0
                                matchedCards = emptySet()
                                flippedCards = emptyList()
                                memoryCards = createCards(cardImages)
                            },
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

                    MemoryCardLayout(memoryCards,
                        flippedCards,
                        matchedCards,
                        score,
                        onCardUpdate = { newFlippedCards, newMatchedCards, newScore ->
                            flippedCards = newFlippedCards
                            matchedCards = newMatchedCards
                            score = newScore
                        })
                }
            }
        }
    }
}

// To create pairs of cards from Images
fun createCards(cardImages: List<Int>): List<MemoryCard> {
    val memoryCards = mutableListOf<MemoryCard>()
    for (img in cardImages) {
        memoryCards.add(MemoryCard(img, img))
        memoryCards.add(MemoryCard(img, img))
    }
    memoryCards.shuffle()
    return memoryCards
}

@Composable
fun MemoryCardLayout(
    memoryCards: List<MemoryCard>,
    revealedCards: List<MemoryCard>,
    matchedCards: Set<Int>,
    score: Int,
    onCardUpdate: (List<MemoryCard>, Set<Int>, Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(3)
    ) {
        items(memoryCards.size) { index ->
            val card = memoryCards[index]
            MemoryCardView(card, onClick = {
                onCardClicked(
                    card, revealedCards, matchedCards, score
                ) { newRevealedCards, newMatchedCards, newScore ->
                    onCardUpdate(newRevealedCards, newMatchedCards, newScore)
                }
            })
        }
    }
}

// When card is clicked, we check whether id matches with the previously revealed card
fun onCardClicked(
    card: MemoryCard,
    revealedCards: List<MemoryCard>,
    matchedCards: Set<Int>,
    score: Int,
    onCardsUpdated: (List<MemoryCard>, Set<Int>, Int) -> Unit
) {
    if (matchedCards.contains(card.id) || revealedCards.contains(card)) return
    val newRevealedCards = revealedCards + card
    val len = newRevealedCards.size
    if (len >= 2 && len % 2 == 0) {
        if (newRevealedCards[len - 1].id == newRevealedCards[len - 2].id) {
            // Match found
            newRevealedCards[len - 1].flipped = true
            card.flipped = true
            onCardsUpdated(
                newRevealedCards, matchedCards + card.id, score + 2
            )
        } else {
            // No match found; keep the revealed cards and reset the last two
            newRevealedCards[len - 2].flipped = false
            val previousRevealed = newRevealedCards.take(newRevealedCards.size - 2)
            onCardsUpdated(previousRevealed, matchedCards, score)
        }
    } else {
        // First card flipped
        newRevealedCards[len - 1].flipped = true
        card.flipped = true
        onCardsUpdated(newRevealedCards, matchedCards, score)
    }

}

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
            painter = painterResource(id = if (card.flipped) card.imgResId else R.drawable.question),
            contentDescription = "Memory Card",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
