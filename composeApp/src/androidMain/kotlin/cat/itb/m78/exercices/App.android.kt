package cat.itb.m78.exercices

import ProjecteMapsAndCamera.NavigationMapsAndCamera
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { NavigationMapsAndCamera() }
    }
}

@Preview
@Composable
fun AppPreview() { NavigationMapsAndCamera() }
