package cat.itb.m78.exercices.Exercicis.BDD

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class messagesViewModel : ViewModel(){
    val messagesQueries = database.messagesQueries
    val id : MutableLongState = mutableLongStateOf(0)

    var all = messagesQueries.selectAll().executeAsList()
    val one = messagesQueries.find(id.value).executeAsList()
    fun insert(msg : String){
        messagesQueries.insert(msg)
    }
    fun update(){
        all = messagesQueries.selectAll().executeAsList()
    }
}

@Composable
fun messagesScreen(){
    val model = viewModel {messagesViewModel()}
    var textMsg by remember { mutableStateOf("") }
    val list = mutableStateOf(model.all)
    Column{
        Row{
            Column {
                TextField(value = textMsg,
                    onValueChange = {textMsg = it},
                    label = {Text("message")})
                Button(onClick = {model.insert(textMsg); model.update(); textMsg = ""}){
                    Text("Add")
                }
            }
            Column {

            }
        }
        LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            itemsIndexed(list.value) { _, message ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,

                        ) {
                        Column(modifier = Modifier.padding(end = 15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                            Text(text = message.message)
                        }
                    }
                }
            }
        }
    }

}
