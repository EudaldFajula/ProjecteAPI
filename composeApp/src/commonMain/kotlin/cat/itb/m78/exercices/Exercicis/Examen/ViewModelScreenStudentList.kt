package cat.itb.m78.exercices.Exercicis.Examen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import cat.itb.m78.exercices.Exercicis.BDD.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {
    //Data base
    val faltesStudents = database.faltesQueries
    fun insert(id: Int, date: String) {
        faltesStudents.insert(id.toLong(), date)
    }


    //Api
    var studentsList = mutableStateOf(listOf<Students>())
    init {
        viewModelScope.launch(Dispatchers.Default) {
            studentsList.value = ApiStudents.list()
        }
    }
    fun searchStudent(student: String) {
        if(student != ""){
            studentsList.value.filter {it.nameStudent.contains(student, ignoreCase = true)}
        }
    }
}