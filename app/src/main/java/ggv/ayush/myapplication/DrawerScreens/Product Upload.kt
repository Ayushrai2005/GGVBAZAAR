package ggv.ayush.myapplication.DrawerScreens

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.io.IOException
import java.util.UUID

@Composable
fun ProductForm() {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }
    var ForRent by remember { mutableStateOf(false) } // Track whether the product is for rent or sell


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Display selected image
        selectedImageUri?.let { uri ->
            Image(
                bitmap = loadBitmap(context.contentResolver, uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        // Button to open gallery and select image
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Select Image")
        }

        // Text field for product title
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Title") },
            modifier = Modifier.fillMaxWidth()
        )

        // Text field for product price
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Product Price") },
            modifier = Modifier.fillMaxWidth()
        )


        // Text field for product description
        TextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )
        // Checkbox for selecting product type (sell or rent)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = ForRent,
                onCheckedChange = { ForRent = it },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = if (ForRent) "For Rent" else "For Rent")
        }


        // Button to upload product data to Firebase
        Button(
            onClick = {
                if (productName.isNotEmpty() && productPrice.isNotEmpty() && productDescription.isNotEmpty() && selectedImageUri != null) {
                    uploadProduct(productName, productDescription, ForRent , productPrice, selectedImageUri!! , context)
                } else {
                    Toast.makeText(context, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Upload Product")
        }
    }
}

private fun loadBitmap(contentResolver: ContentResolver, uri: Uri): ImageBitmap {
    return try {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true)
        resizedBitmap.asImageBitmap()
    } catch (e: IOException) {
        ImageBitmap(1, 1)
    }
}


private fun uploadProduct(name: String, des: String, ForRent: Boolean , price: String, imageUri: Uri, context: Context) {
    val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("images/${name}_${System.currentTimeMillis()}")
    val productId = UUID.randomUUID().toString() // Generate a unique ID for the product

    storageRef.putFile(imageUri)
        .addOnSuccessListener { taskSnapshot ->
            // File upload successful, now get the download URL
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                // Image uploaded successfully, now upload product data to Firestore
                val product = Product(
                    productId = productId,
                    productName = name,
                    forRent = ForRent.toString(),
                    productDescription = des,
                    productPrice = price,
                    productImage = uri.toString()
                )
                Firebase.firestore.collection("Products").document(name)
                    .set(product)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Product uploaded successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to upload product", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}


data class Product (
    val productId : String = "" ,
    val forRent : String = "",
    val productName : String = "",
    val productPrice : String = "",
    val productDescription : String = "",
    val  productImage : String = ""
)