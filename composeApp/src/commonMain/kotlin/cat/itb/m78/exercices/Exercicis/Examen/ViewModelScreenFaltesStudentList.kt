package cat.itb.m78.exercices.Exercicis.Examen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.Exercicis.BDD.database
import cat.itb.m78.exercices.Exercicis.Projecte.Favouritegames
import cat.itb.m78.exercices.db.Faltes
import cat.itb.m78.exercices.db.Freegames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentFaltesViewModel() : ViewModel() {
    //Data base
    val faltesStudents = database.faltesQueries
    var all = faltesStudents.selectAll().executeAsList().map { toXXX(it) }
    var allOrderByDate = faltesStudents.selectAllFilterByDate().executeAsList().map { toXXX(it) }

    //Api
    var studentsList = mutableStateOf(listOf<Students>())
    init {
        viewModelScope.launch(Dispatchers.Default) {
            studentsList.value = ApiStudents.list()
        }
    }


}
private fun toXXX(db: Faltes) = faltes(
    idDataBase = db.id,
    studentId = db.studentId,
    date = db.date
)