package cat.itb.m78.exercices.Exercicis.Projecte

import androidx.compose.runtime.Composable
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
}

@Composable
fun NavigateScreenProject() {
    val navController = rememberNavController()
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
    }
}
