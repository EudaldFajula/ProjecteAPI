package RecuperacioMapsAndCamera

data class InsertMarker(
    var latitude: Double,
    var longitude: Double,
    var title: String,
    var description: String,
    var imageUri: String
)