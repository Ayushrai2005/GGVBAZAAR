package ggv.ayush.myapplication.DrawerScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import ggv.ayush.myapplication.BottomScreens.Lib
import ggv.ayush.myapplication.R
import ggv.ayush.myapplication.Repositories.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

@Composable
fun AccountView() {
    val currentUser = Firebase.auth.currentUser
    val userUid = currentUser?.uid

    // Remember the user state using remember
    val userDataState = remember{ mutableStateOf<User?>(null) }

    // Fetch user data if it hasn't been fetched yet
    if (userUid != null && userDataState.value == null) {
        // Retrieve user data from Firestore only if it hasn't been retrieved yet
        val userDocumentRef = Firebase.firestore.collection("Users").document(Firebase.auth.uid.toString())
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
    // Display user information once it's available
    userDataState.value?.let { userInfo ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//             Circular image
            Image(
                painter = painterResource(id = R.drawable.vector1), // Replace with your local circular image resource
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Name: ${userInfo.name}",
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Phone Number: ${userInfo.phoneNumber}",
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Email Address: ${userInfo.userEmail} ",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}
