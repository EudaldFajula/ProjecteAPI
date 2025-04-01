package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusGameViewModel(id: Int) : ViewModel() {
    var selectedGame = mutableStateOf<Game?>(null)
    init {
        viewModelScope.launch(Dispatchers.Default) {
            selectedGame.value = ApiGame.selectGame(id)
        }
    }

}