import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.memorychecker.model.MemoryCard
import com.example.memorychecker.view.MemoryCardView

@Composable
fun MemoryCardLayout(
    memoryCards: List<MemoryCard>,
    onCardUpdate: (MemoryCard) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(3)
    ) {
        items(memoryCards.size) { index ->
            val card = memoryCards[index]
            MemoryCardView(card) {
                onCardUpdate(card)
            }
        }
    }
}
