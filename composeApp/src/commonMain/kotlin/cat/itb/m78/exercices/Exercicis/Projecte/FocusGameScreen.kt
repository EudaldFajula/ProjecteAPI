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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun FocusGameScreenVM(id: Int, navigateToGeneralScreen: () -> Unit ){
    val model = viewModel { FocusGameViewModel(id) }
    FocusGameScreen(navigateToGeneralScreen, model.selectedGame.value)
}

@Composable
fun FocusGameScreen(navigateToGeneralScreen: () -> Unit, selectedGame: Game?){
    if(selectedGame == null){
        CircularProgressIndicator()
    }else{
        Row(){
            Column(){
                Text("Id: " + selectedGame.idGame)
                Text("Name: " + selectedGame.title)
                AsyncImage(
                    model = selectedGame.image,
                    contentDescription = null,
                    modifier = Modifier.height(300.dp).width(320.dp)
                )
                Text("Description: " + selectedGame.description)
                Text("Genre: " + selectedGame.genre)
                Text("Developer: " + selectedGame.developer)
                Button(onClick = navigateToGeneralScreen){
                    Text("Go Back")
                }
            }
        }
    }
}
