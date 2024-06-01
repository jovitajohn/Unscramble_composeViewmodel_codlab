package com.example.unscramble.ui

import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.GameUiState
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState : StateFlow<GameUiState> = _uiState.asStateFlow()

    lateinit var currentWord : String
    private var usedWord : MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
    private set

    init {
        resetGame()
    }

    fun updateGameState(updatedScore: Int){
        if(usedWord.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }else {
            _uiState.update { currentState ->
                currentState.copy(
                    currentScrambleWord = pickRandomWordAndShuffle(),
                    isGuessWordWrong = false,
                    score = updatedScore,
                    wordCount = currentState.wordCount.inc()
                )
            }
        }
    }

    fun checkUserGuess(){
        if(userGuess.equals(currentWord, ignoreCase = true)){
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }else{
            _uiState.update { currentState ->
                currentState.copy(isGuessWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun updateUserGuess(guess :String){
        userGuess = guess
    }

    fun pickRandomWordAndShuffle() : String {
        currentWord = allWords.random()
        if(usedWord.contains(currentWord)){
            return pickRandomWordAndShuffle()
        }else{
            usedWord.add(currentWord)
            return shuffleWord(currentWord)
        }
    }

    fun shuffleWord(word: String) : String{
        val temp = word.toCharArray()
        temp.shuffle()

        while(temp.equals(word)){
            temp.shuffle()
        }
        return String(temp)
    }

    fun resetGame(){
        usedWord.clear()
        _uiState.value = GameUiState(currentScrambleWord = pickRandomWordAndShuffle())
    }

    fun onSkip(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

}