package ProjecteMapsAndCamera

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter

@Composable
fun AddMarkerScreen(
    navigateToMapScreen: () -> Unit,
    navigateToListMarkerScreen: () -> Unit,
    navigateToAddMarkerScreen: () -> Unit,
    navigateToPermissionScreen: () -> Unit
) {
    // Compartimos el mismo VM de cámara para leer la URI
    val cameraViewModel: CameraViewModel2 = viewModel()

    var textTitle by rememberSaveable { mutableStateOf("") }
    var textX     by rememberSaveable { mutableStateOf("") }
    var textY     by rememberSaveable { mutableStateOf("") }

    // Recogemos el último URI guardado
    val photoUri by cameraViewModel.lastPhotoUri.collectAsState()

    DrawerMenu(
        { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = textTitle,
                    onValueChange = { textTitle = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = textX,
                    onValueChange = { new ->
                        if (new.isEmpty() || new.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            textX = new
                        }
                    },
                    label = { Text("X Coords") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = textY,
                    onValueChange = { new ->
                        if (new.isEmpty() || new.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            textY = new
                        }
                    },
                    label = { Text("Y Coords") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Botón para ir a pedir permisos / cámara
                Button(
                    onClick = { navigateToPermissionScreen() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Take Photo")
                }

                // Miniatura de la foto guardada
                photoUri?.let { uri ->
                    Spacer(Modifier.height(8.dp))
                    Text("Last photo:")
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val x = textX.toDoubleOrNull() ?: 0.0
                        val y = textY.toDoubleOrNull() ?: 0.0
                        // Aquí podrías extender insert para recibir 'photoUri'
                        cameraViewModel.insert(textTitle, y, x, photoUri.toString())
                        navigateToListMarkerScreen()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Add Marker")
                }
            }
        },
        navigateToMapScreen,
        navigateToListMarkerScreen,
        navigateToAddMarkerScreen
    )
}
