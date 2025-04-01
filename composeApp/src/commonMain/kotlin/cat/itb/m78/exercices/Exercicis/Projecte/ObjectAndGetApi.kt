package cat.itb.m78.exercices.Exercicis.Projecte

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Game(@SerialName("id") val idGame: Int,
                @SerialName("title") val title:String,
                @SerialName("thumbnail") val image: String,
                @SerialName("short_description") val description : String,
                @SerialName("game_url") val gameUrl : String,
                @SerialName("genre") val genre : String,
                @SerialName("platform") val platform : String,
                @SerialName("publisher") val publisher : String,
                @SerialName("developer") val developer : String,
                @SerialName("release_date") val releaseDate : String)

object ApiGame {
    private const val URL = "https://www.freetogame.com/api/games"
    private const val URLFind = "https://www.freetogame.com/api/game?id="
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun list() = client.get(URL).body<List<Game>>()
    suspend fun selectGame(id: Int) = client.get(URLFind + id).body<Game>()
}

