package cat.itb.m78.exercices.Exercicis.Examen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.builtins.serializer


@Composable
fun ScreenStudentListVM(navigateToFaltes:() -> Unit) {
    val model = viewModel { StudentViewModel() }
    ScreenStudentList(model.studentsList.value,model::searchStudent, model::insert)
}

@Composable
fun ScreenStudentList(students: List<Students>, searchStudent:(String) -> Unit, insert:(Int, String) -> Unit, ) {
    var text by remember { mutableStateOf("") }
    if (students == null){
        CircularProgressIndicator()
    }else{
        Column {
            TextField(text,
                label = { Text("Search") },
                onValueChange = {
                    text = it
                })
            Button(onClick = {searchStudent(text)}){
                Text("Buscar")
            }
            LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                val filteredList = students.filter { it.nameStudent.contains(text, ignoreCase = true) }
                if (filteredList.isEmpty()) {
                    item {
                        Text("No students found")
                    }
                }

                itemsIndexed(filteredList) { _, student ->
                    Card(modifier = Modifier.fillMaxWidth().clickable(
                        enabled = true,
                        onClickLabel = "Clickable card",
                        onClick = {
                            insert(student.idStudent, Clock.System.now().toString())
                        }))
                    {
                        Text("Id Student: " + student.idStudent)
                        Text("Student Name: " + student.nameStudent)
                        Text("Student Surname: " + student.surnamesStudent)
                        Text("Student Email: " + student.emailStudent)
                        AsyncImage(
                            model = student.photoLinkStudent,
                            contentDescription = null,
                            modifier = Modifier.height(300.dp).width(320.dp)
                        )

                    }
                }
            }
        }
    }
}