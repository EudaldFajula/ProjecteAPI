package cat.itb.m78.exercices.Exercicis

import coil3.compose.AsyncImage


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

@Serializable
data class Countries(@SerialName("media") val media: Flag)

@Serializable
data class Flag(@SerialName("flag") val flag: String)

object MyApiCountries{
    val url = "https://api.sampleapis.com/countries/countries"
    val client = HttpClient(){
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private suspend fun list() = client.get(url).body<List<Countries>>()
    suspend fun changeListCountries(): List<Countries>{
        val listJokes = list()
        return listJokes
    }
}

class ViewModelCountries(): ViewModel(){
    val data = mutableStateOf<Countries?>(null)
    init{
        viewModelScope.launch(Dispatchers.Default){
            data.value = MyApiCountries.changeListCountries().random()
        }
    }
}

@Composable
fun CountriesFun(){
    val viewModel = viewModel { ViewModelCountries() }
    CountriesFun(viewModel.data.value)
}

@Composable
fun CountriesFun(listCountries: Countries?){
    val viewModel = viewModel { ViewModelJokes() }
    if(viewModel.data.value  == null){
        Text("Loading..")
    }else{
        Column{
            AsyncImage(
                model= listCountries!!.media.flag,
                contentDescription = null
            )
        }
    }
}