package ProjecteMapsAndCamera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListMarkerScreen(navigateToMapScreen: () -> Unit, navigateToListMarkerScreen:() -> Unit, navigateToAddMarkerScreen: () -> Unit){
    val model = viewModel { MapScreenVM() }
    DrawerMenu(
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                LazyColumn(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    itemsIndexed(model.all) { _, marker ->

                        Card(modifier = Modifier.fillMaxWidth())
                        {
                            Text("Title: " + marker.title)
                            Text("X: " + marker.x)
                            Text("Y: " + marker.y)
                        }
                    }
                }
            }
        },
        navigateToMapScreen,
        navigateToListMarkerScreen,
        navigateToAddMarkerScreen
    )
}