package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

object Destination {
    @Serializable
    data object GeneralScreenVM
    @Serializable
    data class FocusGameScreenVM(val id: Int)
    @Serializable
    data object FavoriteGamesScreenVM
}


@Composable
fun NavigateScreens(){
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        NavigationBar{
            NavigationBarItem(
                selected = false,
                onClick = {navController.navigate(Destination.GeneralScreenVM)},
                icon = {Icon(imageVector = Icons.Default.Home, contentDescription = null)},
                label = {Text("Home")}
            )
            NavigationBarItem(
                selected = false,
                onClick = {navController.navigate((Destination.FavoriteGamesScreenVM))},
                icon = {Icon(imageVector = Icons.Default.Favorite, contentDescription = null)},
                label = {Text("Favorites games")}
            )
        }
    }){
        NavHost(navController = navController, startDestination = Destination.GeneralScreenVM) {
            composable<Destination.GeneralScreenVM>{
                GeneralScreenVM(
                    navigateToFocusGame = { navController.navigate(Destination.FocusGameScreenVM(it)) }
                )
            }
            composable<Destination.FocusGameScreenVM> { backStack ->
                val id = backStack.toRoute<Destination.FocusGameScreenVM>().id
                FocusGameScreenVM(id, navigateToGeneralScreen = { navController.navigate(Destination.GeneralScreenVM) })
            }
            composable<Destination.FavoriteGamesScreenVM> {
                FavoriteGamesScreenVM(
                    navigateToFavoriteGame = { navController.navigate(Destination.FavoriteGamesScreenVM) }
                )
            }
        }
    }
}