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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ggv.ayush.myapplication.LOGINSIGNUP.Visibility
import ggv.ayush.myapplication.LOGINSIGNUP.VisibilityOff

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
        productNameTextFieldSample(productName = productName , onNameChange = {productName= it})

        productPriceTextFieldSample(productPrice = productPrice , onPriceChange = {productPrice = it})

        productDescriptionTextFieldSample(productDescription = productDescription , onDescriptionChange =  {productDescription = it})

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun productNameTextFieldSample(productName: String, onNameChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = productName,
        onValueChange = { onNameChange(it) },
        shape = RoundedCornerShape(topEnd =12.dp, bottomStart =12.dp),
        label = {
            Text(" Product Title",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            ) },
        placeholder = { Text(text = "Product Title") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // do something here
            }
        )

    )
}@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun productPriceTextFieldSample(productPrice: String, onPriceChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = productPrice,
        onValueChange = { onPriceChange(it) },
        shape = RoundedCornerShape(topEnd =12.dp, bottomStart =12.dp),
        label = {
            Text("Enter Price / Rent for 1 Week",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            ) },
        placeholder = { Text(text = "Enter Price / Rent for 1 Week") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // do something here
            }
        )

    )
}@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun productDescriptionTextFieldSample(productDescription: String, onDescriptionChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = productDescription,
        onValueChange = { onDescriptionChange(it) },
        shape = RoundedCornerShape(topEnd =12.dp, bottomStart =12.dp),
        label = {
            Text("Product Description",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            ) },
        placeholder = { Text(text = "Product Description") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // do something here
            }
        )

    )
}
