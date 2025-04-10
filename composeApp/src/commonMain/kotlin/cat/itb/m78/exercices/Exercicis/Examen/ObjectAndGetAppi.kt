package cat.itb.m78.exercices.Exercicis.Examen

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.DateTimeUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class Students(@SerialName("id") val idStudent: Int,
                @SerialName("name") val nameStudent:String,
                @SerialName("surnames") val surnamesStudent: String,
                @SerialName("email") val emailStudent : String,
                @SerialName("photo_link") val photoLinkStudent : String)

data class faltes(@SerialName("id") val idDataBase: Long,
                @SerialName("studentId") val studentId: Long,
                @SerialName("date") val date: String)

object ApiStudents {
    private const val URL = "https://fp.mateuyabar.com/DAM-M78/composeP2/exam/students.json"
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun list() = client.get(URL).body<List<Students>>()
}
