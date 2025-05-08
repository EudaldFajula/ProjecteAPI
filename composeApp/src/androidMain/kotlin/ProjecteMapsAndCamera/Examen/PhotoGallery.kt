package ProjecteMapsAndCamera.Examen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

private const val COUNT_VIEW_KEY = "name"
class PhotoGalleryViewModel : ViewModel(){
    val settings: Settings = Settings()
    var savePhoto = settings[COUNT_VIEW_KEY, ""]
    var photo = mutableStateOf(savePhoto)
    init {
        settings[COUNT_VIEW_KEY] = photo.value
    }
    fun ChangePhoto(photoInput: String){
        photo.value = photoInput
        settings[COUNT_VIEW_KEY] = photo.value
    }
}

@Composable
fun PhotoGallery(photoInput: String) : String{
    val model = PhotoGalleryViewModel()
    Button(onClick = {model.ChangePhoto(photoInput)}){
        Text("Last Photo")
    }
    return model.photo.value
}