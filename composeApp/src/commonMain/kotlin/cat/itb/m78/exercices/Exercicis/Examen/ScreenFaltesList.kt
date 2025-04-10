package cat.itb.m78.exercices.Exercicis.Examen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.Exercicis.Projecte.FavoriteGameViewModel
import cat.itb.m78.exercices.Exercicis.Projecte.Favouritegames

@Composable
fun ScreenFaltesStudentVM(navigateToStudentList:() -> Unit){
    val model = viewModel { StudentFaltesViewModel() }
    ScreenFaltesStudent(model.allOrderByDate, navigateToStudentList, model.studentsList.value)
}

@Composable
fun ScreenFaltesStudent(students: List<faltes>, navigateToStudentList:() -> Unit, studentsList: List<Students>){
    if (students == null){
        CircularProgressIndicator()
    }else{
        Column {
            LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                itemsIndexed(students) { _, student ->
                    Card(modifier = Modifier.fillMaxWidth())
                    {
                        studentsList.forEach { if(it.idStudent.toLong() == student.studentId){
                            Text("Student Name: " + it.nameStudent + " " + it.surnamesStudent)
                            Text("Date Time: " + student.date)
                        }
                        }
                    }
                }
            }
        }
    }
}