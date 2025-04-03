package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.Exercicis.BDD.database
import cat.itb.m78.exercices.db.Freegames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteGameViewModel() : ViewModel() {
    //Data base
    val gameQueries = database.freeGamesQueries
    var all = gameQueries.selectAll().executeAsList().map { toXXX(it) }
}

private fun toXXX(db: Freegames) = Favouritegames(
    idDataBase = db.id,
    idGame = db.game_id,
    titleGame = db.title_game,
    genreGame = db.genre_game
)
