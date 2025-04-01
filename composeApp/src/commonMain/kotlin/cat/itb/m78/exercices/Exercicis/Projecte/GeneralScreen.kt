package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun GeneralScreenVM(navigateToFocusGame: (id: Int) -> Unit) {
    val model = viewModel { GamesViewModel() }
    GeneralScreen(model.gameList.value, navigateToFocusGame)
}

@Composable
fun GeneralScreen(games: List<Game>, navigateToFocusGame: (id: Int) -> Unit) {
    var text by remember { mutableStateOf("") }
    if (games == null){
        CircularProgressIndicator()
    }else{
        LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            itemsIndexed(games) { _, game ->
                Column {
                    TextField(text,
                        label = { Text("Search") },
                        onValueChange = {
                            text = it
                        })
                    Button(onClick = {}){
                        Text("Buscar")
                    }
                }
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(end = 15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        Button(onClick = {navigateToFocusGame(game.idGame)}, modifier = Modifier.height(500.dp).fillMaxWidth()){
                            Column {
                                Text( "Game Id: " +  game.idGame)
                                AsyncImage(
                                    model = game.image,
                                    contentDescription = null,
                                    modifier = Modifier.height(300.dp).width(320.dp)
                                )
                                Text( "Game Title: " +  game.title)
                                Text("Game Genre: " + game.genre)
                            }
                        }
                    }

                }
            }
        }
    }
}