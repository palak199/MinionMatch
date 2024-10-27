package com.example.memorychecker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.memorychecker.constants.MemoryCardImages
import com.example.memorychecker.model.MemoryCard

class MemoryCardViewModel : ViewModel() {
    var score = mutableStateOf(0)
    var memoryCards = mutableStateListOf<MemoryCard>()
    var flippedCards = mutableStateListOf<MemoryCard>()
    var matchedCards = mutableSetOf<Int>()

    init {
        resetGame()
    }

    fun resetGame() {
        score.value = 0
        matchedCards.clear()
        flippedCards.clear()
        memoryCards.clear()
        memoryCards.addAll(createCards(MemoryCardImages.images))
    }

    private fun createCards(cardImages: List<Int>): List<MemoryCard> {
        val memoryCards = mutableListOf<MemoryCard>()
        for (img in cardImages) {
            memoryCards.add(MemoryCard(img, img))
            memoryCards.add(MemoryCard(img, img))
        }
        memoryCards.shuffle()
        return memoryCards
    }

    fun onCardClicked(card: MemoryCard) {
        if (matchedCards.contains(card.id) || flippedCards.contains(card)) return
        flippedCards.add(card)
        val len = flippedCards.size
        if (len >= 2 && len % 2 == 0) {
            if (flippedCards[len - 1].id == flippedCards[len - 2].id) {
                // Match found
                flippedCards[len - 1].flipped.value = true
                card.flipped.value = true
                matchedCards.add(card.id)
                score.value += 2
            } else {
                // No match found; keep the revealed cards and reset the last two
                flippedCards[len - 2].flipped.value = false
                flippedCards.removeAt(len - 1)
                flippedCards.removeAt(len - 2)
            }
        } else {
            // First card flipped
            card.flipped.value = true
        }
    }
}