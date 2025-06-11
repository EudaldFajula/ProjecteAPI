package RecuperacioMapsAndCamera

import RecuperacioMapsAndCamera.Screens.AddMarkerScreen
import RecuperacioMapsAndCamera.Screens.CameraScreen
import RecuperacioMapsAndCamera.Screens.DetailScreen
import RecuperacioMapsAndCamera.Screens.ListScreen
import RecuperacioMapsAndCamera.Screens.MapsScreen
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

const val PHOTO_URI_KEY = "photoUri"

object Destination {
    @Serializable
    data object MapsScreen
    @Serializable
    data class CameraScreen(val latitude: Float, val longitude: Float)
    @Serializable
    data class AddMarkerScreen(val latitude: Float, val longitude: Float)
    @Serializable
    data object ListScreen
    @Serializable
    data class DetailScreen(val id: Int)
}

@Composable
fun MapsAndCamera() {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
                    Text("Markers", modifier = Modifier.padding(start = 8.dp))
                }

                HorizontalDivider()

                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(imageVector = Icons.Filled.Place, contentDescription = null)
                            Text("Map", modifier = Modifier.padding(start = 8.dp))
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(Destination.MapsScreen)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                            Text("Saved markers", modifier = Modifier.padding(start = 8.dp))
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(Destination.ListScreen)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ){
        NavHost(navController = navController, startDestination = Destination.MapsScreen ){
            composable<Destination.MapsScreen> {
                MapsScreen(
                    navigateToAddMarkerScreen = { latitude : Double, longitude : Double ->
                        navController.navigate(Destination.AddMarkerScreen(latitude.toFloat(), longitude.toFloat()))
                    },
                    drawerState = drawerState,
                    scope = scope)
            }

            composable<Destination.ListScreen> {
                ListScreen(
                    navigateToDetailScreen = { markerId: Int ->
                        navController.navigate(Destination.DetailScreen(id = markerId))
                    },
                    drawerState = drawerState,
                    scope = scope
                )
            }

            composable<Destination.DetailScreen> { backStack ->
                val markerId = backStack.toRoute<Destination.DetailScreen>().id

                DetailScreen(
                    markerId = markerId,
                    navController = navController
                )
            }

            composable<Destination.AddMarkerScreen> { backStack ->
                val savedStateHandle = backStack.savedStateHandle
                val latitude = backStack.toRoute<Destination.AddMarkerScreen>().latitude
                val longitude = backStack.toRoute<Destination.AddMarkerScreen>().longitude

                val latLng = LatLng(latitude.toDouble(), longitude.toDouble())

                AddMarkerScreen(
                    savedStateHandle,
                    navigateToMapScreen = { navController.navigate(Destination.MapsScreen) },
                    navigateToCameraScreen = {
                        navController.navigate(Destination.CameraScreen(latitude, longitude))
                    },
                    latLng = latLng
                )
            }

            composable<Destination.CameraScreen> { backStack ->
                val latitude = backStack.toRoute<Destination.CameraScreen>().latitude
                val longitude = backStack.toRoute<Destination.CameraScreen>().longitude

                CameraScreen(
                    navigateToAddMarkerScreen = { },
                    navController = navController,
                    latLng = LatLng(latitude.toDouble(), longitude.toDouble())
                )
            }
        }
    }
}