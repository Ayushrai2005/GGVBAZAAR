package ggv.ayush.myapplication.DrawerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ggv.ayush.myapplication.R

@Composable
fun AccountView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Account information
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Account",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text("Panjutorials", style = MaterialTheme.typography.h6)
                    Text(
                        "@tutorialsEU",
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.Gray
                    )
                }
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }

        // Divider
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color.LightGray
        )

        // My Music section
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_music_video_24),
                contentDescription = "My Music",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "My Music",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Divider(color = Color.LightGray)

        // Add more sections or features as needed...

    }
}
