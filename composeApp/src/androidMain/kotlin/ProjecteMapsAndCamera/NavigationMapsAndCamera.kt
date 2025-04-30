package ProjecteMapsAndCamera

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


object MapsAndCamera{
    @Serializable
    data object DrawerMenu
    @Serializable
    data object PermisionScreen
    @Serializable
    data object MapScreen
    @Serializable
    data object ListMarkerScreen
    @Serializable
    data object AddMarkerScreen

}
@Composable
fun NavigationMapsAndCamera() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MapsAndCamera.PermisionScreen) {
        composable<MapsAndCamera.DrawerMenu>{
            DrawerMenu(
                content = {},
                navigateToMapScreen = { navController.navigate(MapsAndCamera.MapScreen) },
                navigateToListMarkerScreen = { navController.navigate(MapsAndCamera.ListMarkerScreen) },
                navigateToAddMarkerScreen = { navController.navigate(MapsAndCamera.AddMarkerScreen) }
            )
        }
        composable<MapsAndCamera.PermisionScreen> {
            PermisionScreen(
                navigateToMapScreen = { navController.navigate(MapsAndCamera.MapScreen) },
                navigateToListMarkerScreen = { navController.navigate(MapsAndCamera.ListMarkerScreen) },
                navigateToAddMarkerScreen = { navController.navigate(MapsAndCamera.AddMarkerScreen) }
            )
        }
        composable<MapsAndCamera.MapScreen> {
            MapScreen(
                navigateToMapScreen = { navController.navigate(MapsAndCamera.MapScreen) },
                navigateToListMarkerScreen = { navController.navigate(MapsAndCamera.ListMarkerScreen) },
                navigateToAddMarkerScreen = { navController.navigate(MapsAndCamera.AddMarkerScreen) }
            )
        }
        composable<MapsAndCamera.ListMarkerScreen> {
            ListMarkerScreen(
                navigateToMapScreen = { navController.navigate(MapsAndCamera.MapScreen) },
                navigateToListMarkerScreen = { navController.navigate(MapsAndCamera.ListMarkerScreen) },
                navigateToAddMarkerScreen = { navController.navigate(MapsAndCamera.AddMarkerScreen) }
            )

        }
        composable<MapsAndCamera.AddMarkerScreen> {
            AddMarkerScreen(
                navigateToMapScreen = { navController.navigate(MapsAndCamera.MapScreen) },
                navigateToListMarkerScreen = { navController.navigate(MapsAndCamera.ListMarkerScreen) },
                navigateToAddMarkerScreen = { navController.navigate(MapsAndCamera.AddMarkerScreen) }
            )

        }
    }
}