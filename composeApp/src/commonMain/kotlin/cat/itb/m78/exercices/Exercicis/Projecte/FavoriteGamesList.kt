package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun FavoriteGamesScreenVM(navigateToFavoriteGame: () -> Unit){
    val model = viewModel { FavoriteGameViewModel() }
    FavoriteGamesScreen(model.all)
}

@Composable
fun FavoriteGamesScreen(games: List<Favouritegames>){
    if (games == null){
        CircularProgressIndicator()
    }else{
        Column {
            LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                itemsIndexed(games) { _, game ->

                    Card(modifier = Modifier.fillMaxWidth())
                    {
                        Text("Game Id: " + game.idGame)
                        Text("Game Title: " + game.titleGame)
                        Text("Game Genre: " + game.genreGame)
                    }
                }
            }
        }
    }
}