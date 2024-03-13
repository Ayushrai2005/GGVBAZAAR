import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.navigation.NavController

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter

import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import ggv.ayush.myapplication.DrawerScreens.Product
import ggv.ayush.myapplication.R
import ggv.ayush.myapplication.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(navController : NavController) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Fetch products from Firestore
    LaunchedEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val productList = mutableListOf<Product>()
        val result = firestore.collection("Products").get().await()
        for (document in result) {
            val product = document.toObject(Product::class.java)
            productList.add(product)
        }
        products = productList
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        BackgroundImage(
            painter = painterResource(id = R.drawable.img3),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize() // Set the modifier to fill the entire screen
        )

        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier = Modifier.padding(16.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product) {
                    navController.navigate("${Screen.ProductDetail.route}/${product.productName}")
                }
            }
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


@Composable
fun ProductCard(product: Product , onItemClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = { onItemClick(product.productName) }), // Making the card clickable
        elevation = 16.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            // Product image
            DownloadedImage(product.productImage)
            // Product name/title
            Text(
                text = product.productName,
                modifier = Modifier.padding(top = 8.dp),
            )

            // Product price
            Text(
                text = "INR ${product.productPrice}",
                modifier = Modifier.padding(top = 4.dp),
            )
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
fun DownloadedImage(url: String) {
    val scope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(url) {
        scope.launch {
            bitmap = downloadImage(url)
            isLoading = false
        }
    }

    if (isLoading) {
        // Display circular loader while image is being downloaded
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
        )
    } else {
        bitmap?.let { loadedBitmap ->
            Image(bitmap = loadedBitmap.asImageBitmap(), contentDescription = null)
        } ?: run {
            // Placeholder image if download fails
            Image(painter = painterResource(id = R.drawable.avtar), contentDescription = null)
        }
    }
}
