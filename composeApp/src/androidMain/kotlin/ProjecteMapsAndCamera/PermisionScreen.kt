package ProjecteMapsAndCamera

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermisionScreen(navigateToMapScreen: () -> Unit){
    DrawerMenu(content = {innerPadding -> Column(modifier = Modifier.padding(innerPadding)){
        Text("Holi")
    } })
}