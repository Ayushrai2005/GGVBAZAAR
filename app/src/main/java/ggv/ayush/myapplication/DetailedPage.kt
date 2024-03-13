package ggv.ayush.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import ggv.ayush.myapplication.DrawerScreens.Product
import ggv.ayush.myapplication.ui.theme.PrimaryColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min

@Composable
fun DetailPage(productName: String) {
    val context = LocalContext.current
    // ViewModel or other logic to retrieve product details based on productId
    // For now, let's assume you have a method to retrieve product details based on productId
    // Observe changes in product details
    val firestore = FirebaseFirestore.getInstance()
    var products by remember { mutableStateOf<Product?>(null) } // Declare product variable here


    // Fetch products from Firestore
    LaunchedEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val productDocument  = firestore.collection("Products").document(productName).get().await()
        products = productDocument.toObject(Product::class.java)
    }


    // Render UI based on product details
    products?.let { product ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Product image
            DownloadedImage(
                url = product.productImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Set aspect ratio to maintain square shape
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Product title
            Text(
                text = product.productName,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                fontSize = 20.sp, // Adjust the font size as needed
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(14.dp))

            // Product price
            Text(
                text = "Price: Rs ${product.productPrice}",
                color = Color.Black,
                fontSize = 20.sp, // Adjust the font size as needed
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product description
            Text(
                text = product.productDescription,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp, // Adjust the font size as needed
                color = Color.Gray ,
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                    .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = androidx.compose.material.MaterialTheme.colors.PrimaryColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 30.dp, bottom = 30.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    onClick = {
                        Toast.makeText(
                            context,
                            "Successfully added to cart",
                            Toast.LENGTH_SHORT
                        ).show()

                    },
                ) {
                    androidx.compose.material.Text(text = "Add to Cart", fontSize = 16.sp)
                }
            }
        }

    }
}

// Function to download an image from URL and convert it into a Bitmap
suspend fun downloadImage(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        var inputStream: InputStream? = null
        var connection: HttpURLConnection? = null
        try {
            val imageUrl = URL(url)
            connection = imageUrl.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            connection?.disconnect()
        }
        return@withContext bitmap
    }
}
@Composable
fun DownloadedImage(url: String , modifier: Modifier = Modifier) {
    val scope = MainScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        scope.launch {
            bitmap = downloadImage(url)
        }
    }

    bitmap?.let { loadedBitmap ->
        val scaledBitmap = scaleBitmapToSquare(loadedBitmap)
        Image(
            bitmap = scaledBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.FillBounds // Ensure the image fills the square bounds
        )    } ?: run {
        // Placeholder image while loading or if download fails
        Image(
            painter = painterResource(id = R.drawable.avtar),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.FillBounds // Ensure the placeholder fills the square bounds
        )    }
}
// Function to scale the bitmap to fit within a square without distortion
private fun scaleBitmapToSquare(bitmap: Bitmap): Bitmap {
    val size = min(bitmap.width, bitmap.height)
    val scaledBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(scaledBitmap)
    val dstRect = Rect(0, 0, size, size)
    canvas.drawBitmap(bitmap, null, dstRect, null)
    return scaledBitmap
}