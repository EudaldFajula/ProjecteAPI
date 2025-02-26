package cat.itb.m78.exercices.Exercicis

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

private const val COUNT_VIEW_KEY = "count"
class CountViewViewModel : ViewModel(){
    val settings: Settings = Settings()
    val countViews = settings.getInt(COUNT_VIEW_KEY, 0)
    init {
        settings[COUNT_VIEW_KEY] = countViews+1
    }
}

@Composable
fun CountViewViewScreen(){
    val viewModel = viewModel { CountViewViewModel() }
    CountViewViewScreen(viewModel.countViews)
}

@Composable
fun CountViewViewScreen(countViews: Int) {
    Text("You have opened this app $countViews times")
}