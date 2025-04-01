package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel() {
    var gameList = mutableStateOf(listOf<Game>())
    init {
        viewModelScope.launch(Dispatchers.Default) {
            gameList.value = ApiGame.list()
        }
    }
}