package RecuperacioMapsAndCamera.ViewModels

import RecuperacioMapsAndCamera.InsertMarker
import RecuperacioMapsAndCamera.PHOTO_URI_KEY
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cat.itb.m78.exercices.sqldelight.database

class AddMarkerViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(){
    //Database
    private val dbQueries = database.mapsQueries

    fun addMarker(marker : InsertMarker){
        dbQueries.insert(
            marker.latitude,
            marker.longitude,
            marker.title,
            marker.description,
            marker.imageUri
        )
    }

    //Photo URI
    val photoUri = savedStateHandle.getStateFlow<String?>(PHOTO_URI_KEY, null)

    //State between screens
    private var tempMarker = InsertMarker(0.0, 0.0, "", "", "")

    fun saveMarkerState(marker: InsertMarker) {
        tempMarker = marker
    }

    fun getMarkerState(): InsertMarker {
        return tempMarker
    }
}