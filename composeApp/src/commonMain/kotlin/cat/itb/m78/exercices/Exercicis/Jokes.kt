package cat.itb.m78.exercices.Exercicis

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import cat.itb.m78.exercices.Exercicis.MyApiJokes.changeListJokes as changeListJokes1

@Serializable
data class Jokes(@SerialName("id") val id: Int, @SerialName("type") val typeJoke: String, @SerialName("setup") val inicJoke: String, @SerialName("punchline") val finishJoke: String)

object MyApiJokes{
     val url = "https://api.sampleapis.com/jokes/goodJokes"
     val client = HttpClient(){
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private suspend fun list() = client.get(url).body<List<Jokes>>()
    suspend fun changeListJokes(): List<Jokes>{
        val listJokes = list()
        return listJokes
    }
}

class ViewModelJokes(): ViewModel(){
    val data = mutableStateOf<Jokes?>(null)
    init{
        viewModelScope.launch(Dispatchers.Default){
            data.value = MyApiJokes.changeListJokes().random()
        }
    }
}

@Composable
fun JokesFun(){
    val viewModel = viewModel { ViewModelJokes() }
    JokesFun(viewModel.data.value)
}

@Composable
fun JokesFun(listJokes: Jokes?){
    val viewModel = viewModel { ViewModelJokes() }
    if(viewModel.data.value  == null){
        Text("Loading..")
    }else{
        Column{
            Text("Setup: " + listJokes!!.inicJoke)
            Text("Punchline: " + listJokes!!.finishJoke)
        }
    }
}