package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.Exercicis.BDD.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusGameViewModel(id: Int) : ViewModel() {
    //API
    var selectedGame = mutableStateOf<Game?>(null)
    init {
        viewModelScope.launch(Dispatchers.Default) {
            selectedGame.value = ApiGame.selectGame(id)
        }
    }
    //Data base
    val gameQueries = database.freeGamesQueries

    var all = gameQueries.selectAll().executeAsList()
    val one = gameQueries.findByGameId(id.toLong()).executeAsList()
    fun insert(id: Int, game_title : String, game_genre : String){
        gameQueries.insert(id.toLong(), game_title, game_genre)
    }
}