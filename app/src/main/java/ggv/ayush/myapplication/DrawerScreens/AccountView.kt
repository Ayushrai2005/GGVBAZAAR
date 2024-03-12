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
import com.google.firebase.firestore.firestore
import ggv.ayush.myapplication.BottomScreens.Lib
import ggv.ayush.myapplication.LOGINSIGNUP.User
import ggv.ayush.myapplication.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AccountView() {
    val currentUser = Firebase.auth.currentUser
    val userUid = currentUser?.uid

    // Remember the user state using remember
    val user = remember{ mutableStateOf<User?>(null) }

    if (userUid != null && user.value == null) {
        // Retrieve user data from Firestore only if it hasn't been retrieved yet
        val userDocumentRef = Firebase.firestore.collection("Users").document(userUid)
        userDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve user data
                    val userData = documentSnapshot.toObject(User::class.java)
                    // Update the user state
                    user.value = userData
                } else {
                    // Document does not exist
                    // Handle the case accordingly
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    // Display user information once it's available
    user.value?.let { userInfo ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular image
            Image(
                painter = painterResource(id = R.drawable.avtar), // Replace with your local circular image resource
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
                text = "Email Address: ${userInfo.userEmail}",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}
