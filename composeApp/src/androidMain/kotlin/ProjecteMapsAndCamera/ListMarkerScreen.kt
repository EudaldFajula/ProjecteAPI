package ProjecteMapsAndCamera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun ListMarkerScreen(
    navigateToMapScreen: () -> Unit,
    navigateToListMarkerScreen: () -> Unit,
    navigateToAddMarkerScreen: () -> Unit
) {
    val model: MapScreenVM = viewModel()
    DrawerMenu(
        { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(model.all) { marker ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // 1) If marker.imageUrl is non-null, display it:
                                marker.url?.let { url ->
                                    AsyncImage(
                                        model = url,
                                        contentDescription = "Marker photo",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                Text("Title: ${marker.title}")
                                Text("X: ${marker.x}")
                                Text("Y: ${marker.y}")

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {  },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Delete")
                                }
                            }
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