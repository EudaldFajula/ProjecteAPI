package ProjecteMapsAndCamera

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddMarkerScreen(navigateToMapScreen: () -> Unit, navigateToListMarkerScreen:() -> Unit, navigateToAddMarkerScreen: () -> Unit){
    val model = viewModel { MapScreenVM() }
    DrawerMenu(
        content = { innerPadding ->
            var textTitle by remember{ mutableStateOf("")}
            var textX by remember { mutableStateOf("") }
            var textY by remember { mutableStateOf("") }
            Column(modifier = Modifier.padding(innerPadding)) {
                TextField(
                    value = textTitle,
                    onValueChange = {textTitle = it},
                    label = {Text("Title")}
                )
                TextField(
                    value = textX,
                    onValueChange = {textX = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    label = {Text("X Coords")}
                )
                TextField(
                    value = textY,
                    onValueChange = {textY = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    label = {Text("Y Coords")}
                )
                Button(onClick = {model.insert(textTitle, textY.toDouble(), textX.toDouble())} ){
                    Text("Add Marker")
                }
            }
        },
        navigateToMapScreen,
        navigateToListMarkerScreen,
        navigateToAddMarkerScreen
    )
}