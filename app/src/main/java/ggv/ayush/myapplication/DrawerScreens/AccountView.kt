package ggv.ayush.myapplication.DrawerScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import ggv.ayush.myapplication.R
import ggv.ayush.myapplication.Repositories.User
import kotlinx.coroutines.tasks.await


@Composable
fun AccountView() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userEmail = currentUser!!.email

    // Card background color
    val cardBackgroundColor = Color(0xFFE6E6FA) // Lavender color

    val userDataState = remember{ mutableStateOf<User?>(null) }

    // Fetch user data if it hasn't been fetched yet
    if (userEmail != null && userDataState.value == null) {
        // Retrieve user data from Firestore only if it hasn't been retrieved yet
        val userDocumentRef = Firebase.firestore.collection("Users").document(userEmail)
        LaunchedEffect(userDocumentRef) {
            try {
                val documentSnapshot = userDocumentRef.get().await()
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve user data
                    val userData = documentSnapshot.toObject<User>()
                    // Update the user state
                    userDataState.value = userData
                } else {
                    // Document does not exist
                    // Handle the case accordingly
                }
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    @Composable
    fun BackgroundImage(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.FillBounds,
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }



    // Display user information once it's available
    userDataState.value?.let { userInfo ->
        Box(modifier = Modifier.fillMaxSize()) {

            BackgroundImage(
                painter = painterResource(id = R.drawable.profilebkg),
                contentDescription = "Background Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize() // Set the modifier to fill the entire screen
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                Column(
                    modifier = Modifier.padding(top = 20.dp)
                ) {
//                    Circular image
                            Image(
                                painter = painterResource(id = R.drawable.radhika), // Replace with your local circular image resource
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                }
                Column(
                    modifier = Modifier.padding( bottom = 100.dp)
                ) {
                        // Name text
                        Text(
                            text = "Name:",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = userInfo.name,
                            style = MaterialTheme.typography.subtitle1,
                            color = Color(0xFF9370DB) // Lavender color
                        )

                        // Phone number text
                        Text(
                            text = "Phone Number:",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = userInfo.phoneNumber,
                            style = MaterialTheme.typography.subtitle1,
                            color = Color(0xFF9370DB) // Lavender color
                        )

                        // Email address text
                        Text(
                            text = "Email Address:",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = userInfo.userEmail,
                            style = MaterialTheme.typography.subtitle1,
                            color = Color(0xFF9370DB) // Lavender color
                        )
                    }
                }
            }
        }
    }

