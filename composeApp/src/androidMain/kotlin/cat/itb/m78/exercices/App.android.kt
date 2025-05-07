package cat.itb.m78.exercices

import ProjecteMapsAndCamera.NavigationMapsAndCamera
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cat.itb.m78.exercices.theme.ContextProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextProvider.init(this)
        enableEdgeToEdge()
        setContent {
            // ← ensure this is your root Composable:
            NavigationMapsAndCamera()
        }
    }
}