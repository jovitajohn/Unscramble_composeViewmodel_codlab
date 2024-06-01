package com.example.unscramble.data

data class GameUiState(
    val currentScrambleWord: String = " ",
    val isGuessWordWrong : Boolean = false,
    val score : Int = 0,
    val wordCount : Int = 1,
    val isGameOver : Boolean = false
)

