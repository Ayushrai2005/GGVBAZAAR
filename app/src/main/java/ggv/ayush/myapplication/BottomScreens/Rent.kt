package ggv.ayush.myapplication.BottomScreens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import ggv.ayush.myapplication.DrawerScreens.Product
import ggv.ayush.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForRent() {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Fetch products from Firestore
    LaunchedEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val productList = mutableListOf<Product>()
        val result = firestore.collection("Products").get().await()
        for (document in result) {
            val product = document.toObject(Product::class.java)
            if(product.forRent == "true"){
                productList.add(product)
            }
        }
        products = productList
    }


    LazyVerticalGrid(
        GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product)
        }
    }
}


@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.padding(8.dp),
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
    val scope = MainScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        scope.launch {
            bitmap = downloadImage(url)
        }
    }

    bitmap?.let { loadedBitmap ->
        Image(bitmap = loadedBitmap.asImageBitmap(), contentDescription = null)
    } ?: run {
        // Placeholder image while loading or if download fails
        Image(painter = painterResource(id = R.drawable.avtar), contentDescription = null)
    }
}