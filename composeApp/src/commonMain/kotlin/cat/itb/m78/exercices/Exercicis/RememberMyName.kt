package cat.itb.m78.exercices.Exercicis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import m78exercices.composeapp.generated.resources.Res

private const val COUNT_VIEW_KEY = "name"
class RememberMyNameViewViewModel : ViewModel(){
    val settings: Settings = Settings()
    var saveName = settings[COUNT_VIEW_KEY, ""]
    var name = mutableStateOf(saveName)
    init {
        settings[COUNT_VIEW_KEY] = name.value
    }
    fun ChangeUserName(nameInput: String){
        name.value = nameInput
        settings[COUNT_VIEW_KEY] = name.value
    }
}

@Composable
fun RememberMyNameViewScreen(){
    val viewModel = viewModel { RememberMyNameViewViewModel() }
    RememberMyNameViewScreen(viewModel.saveName)
}

@Composable
fun RememberMyNameViewScreen(saveName: String) {
    val viewModel = viewModel { RememberMyNameViewViewModel() }
    var usuari by remember{ mutableStateOf("")}
    Column(){
        OutlinedTextField (
            value = usuari,
            onValueChange = { usuari = it },
            label = {
                Text("Nombre de usuario")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            singleLine = true
        )
        Button(onClick = {viewModel.ChangeUserName(usuari)}){
            Text("Guardar")
        }
        Text("Helo " + viewModel.saveName)
    }

}