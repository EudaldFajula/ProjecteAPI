package cat.itb.m78.exercices.Exercicis.Examen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cat.itb.m78.exercices.Exercicis.Projecte.FavoriteGamesScreenVM
import cat.itb.m78.exercices.Exercicis.Projecte.FocusGameScreenVM
import cat.itb.m78.exercices.Exercicis.Projecte.GeneralScreenVM
import kotlinx.serialization.Serializable


object Destination {
    @Serializable
    data object ScreenStudentListVM
    @Serializable
    data object ScreenFaltesStudentVM
    @Serializable
    data object ScreenStudentListExtensioVM
}


@Composable
fun NavigateScreens(){
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        NavigationBar{
            NavigationBarItem(
                selected = false,
                onClick = {navController.navigate(Destination.ScreenStudentListVM)},
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = false,
                onClick = {navController.navigate((Destination.ScreenFaltesStudentVM))},
                icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = null) },
                label = { Text("Faltes Student List") }
            )
            NavigationBarItem(
                selected = false,
                onClick = {navController.navigate((Destination.ScreenStudentListExtensioVM))},
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                label = { Text("Extensio 2") }
            )
        }
    }){
        NavHost(navController = navController, startDestination = Destination.ScreenStudentListVM) {
            composable<Destination.ScreenStudentListVM>{
                ScreenStudentListVM(
                    navigateToFaltes = { navController.navigate(Destination.ScreenFaltesStudentVM) }
                )
            }
            composable<Destination.ScreenFaltesStudentVM> {
                ScreenFaltesStudentVM(
                    navigateToStudentList = { navController.navigate(Destination.ScreenStudentListVM) }
                )
            }
            composable<Destination.ScreenStudentListExtensioVM> {
                ScreenStudentListExtensioVM(
                    navigateToStudentList2 = { navController.navigate(Destination.ScreenStudentListExtensioVM) }
                )
            }
        }

    }
}