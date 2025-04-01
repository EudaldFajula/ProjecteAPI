package cat.itb.m78.exercices.Exercicis

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object Destination {
    @Serializable
    data object Screen0
    @Serializable
    data object Screen1
    @Serializable
    data object Screen2
    @Serializable
    data class Screen3(val message: String)
}


@Serializable
data class EstatEmbassament(@SerialName("dia") val day: String, @SerialName("estaci") val place: String, @SerialName("nivell_absolut") val absolutLevel: Double,@SerialName("percentatge_volum_embassat") val volumPercentage: Double,@SerialName("volum_embassat") val volumEmbassat: Double,)

object MyApi {
    private const val URL = "https://analisi.transparenciacatalunya.cat/resource/gn9e-3qhr.json"
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun list() = client.get(URL).body<List<EstatEmbassament>>()
}

class EstatEmbassamentViewModel : ViewModel() {

    var estatEmbassamentList = mutableStateOf(listOf<EstatEmbassament>())
    init {
        viewModelScope.launch(Dispatchers.Default) {
            estatEmbassamentList.value = MyApi.list()
        }
    }
}

@Composable
fun EstatEmbassamentFun() {
    val model = viewModel {EstatEmbassamentViewModel()}
    EstatEmbassamentScreen1(model.estatEmbassamentList.value)
}
@Composable
fun EstatEmbassamentScreen1(estatEmbassament: List<EstatEmbassament>) {
    if (estatEmbassament == null){
        CircularProgressIndicator()
    }else{
        LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            itemsIndexed(estatEmbassament) { _, estatEmbassament ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                        Column(modifier = Modifier.padding(end = 15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                            Button(onClick = {}){
                                Text("Dia: " + estatEmbassament.day)
                                Text(" Estaci: " + estatEmbassament.place)
                                Text(" Nivell Absolut: " + estatEmbassament.absolutLevel)
                                Text(" Percentatge Volum Embassat: " + estatEmbassament.volumPercentage)
                                Text(" Volum Embassat: " + estatEmbassament.volumEmbassat)
                            }
                        }
                    }
                }
            }
        }
    }
}