package RecuperacioMapsAndCamera.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cat.itb.m78.exercices.db.SaveMarkers
import cat.itb.m78.exercices.sqldelight.database

class DetailViewModel(markerId: Int) : ViewModel() {
    private val dbQueries = database.mapsQueries
    private val marker1 = SaveMarkers(0, 0.0, 0.0, "", "", "")

    var marker = mutableStateOf(marker1)

    init {
        try {
            val loadedMarker = dbQueries.selectById(markerId.toLong()).executeAsOne()
            marker.value = loadedMarker

            Log.d("DetailViewModel", "Loaded marker: $markerId")
            Log.d("DetailViewModel", "Image URI: ${loadedMarker.imageUri}")
            if (loadedMarker.imageUri.isNullOrEmpty()) {
                Log.w("DetailViewModel", "Marker has empty image URI")
            }
        } catch (e: Exception) {
            Log.e("DetailViewModel", "Error loading marker: ${e.message}", e)
        }
    }
}