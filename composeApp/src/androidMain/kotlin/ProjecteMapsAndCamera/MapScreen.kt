package ProjecteMapsAndCamera

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.Exercicis.Examen.StudentFaltesViewModel
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen(navigateToMapScreen: () -> Unit, navigateToListMarkerScreen:() -> Unit, navigateToAddMarkerScreen: () -> Unit){
    val model = viewModel { MapScreenVM() }
    DrawerMenu(
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text("Pantalla Mapa")
                GoogleMap(
                    googleMapOptionsFactory = {
                        GoogleMapOptions().mapId("DEMO_MAP_ID")
                    },
                ) {
                    model.all.forEach {
                        AdvancedMarker(
                            state = MarkerState(position = LatLng(it.x, it.y)),
                            title = it.title
                        )
                    }

                }
            }
        },
        navigateToMapScreen,
        navigateToListMarkerScreen,
        navigateToAddMarkerScreen
    )
}