package ProjecteMapsAndCamera

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen(navigateToMapScreen: () -> Unit, navigateToListMarkerScreen:() -> Unit, navigateToAddMarkerScreen: () -> Unit){
    DrawerMenu(
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text("Pantalla Mapa")
                GoogleMap(
                    googleMapOptionsFactory = {
                        GoogleMapOptions().mapId("DEMO_MAP_ID")
                    },
                ) {
                    AdvancedMarker(
                        state = MarkerState(position = LatLng(-34.0, 151.0)),
                        title = "Marker in Sydney"
                    )
                    AdvancedMarker(
                        state = MarkerState(position = LatLng(35.66, 139.6)),
                        title = "Marker in Tokyo"
                    )
                }
            }
        },
        navigateToMapScreen,
        navigateToListMarkerScreen,
        navigateToAddMarkerScreen
    )
}