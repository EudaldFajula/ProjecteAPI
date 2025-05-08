package RecuperacioMapsAndCamera.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.db.SaveMarkers
import cat.itb.m78.exercices.sqldelight.database
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val dbQueries = database.mapsQueries

    var markerList = mutableStateOf<List<SaveMarkers>>(emptyList())

    init {
        viewModelScope.launch {
            markerList.value = dbQueries.selectAll().executeAsList()
        }
    }

    fun deleteMarker(markerId: Int) {
        viewModelScope.launch {
            dbQueries.delete(markerId.toLong())
            markerList.value = dbQueries.selectAll().executeAsList()
        }
    }
}