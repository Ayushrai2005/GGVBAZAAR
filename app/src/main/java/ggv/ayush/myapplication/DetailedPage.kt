package ggv.ayush.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import ggv.ayush.myapplication.Repositories.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun DetailPage(productId: String  , viewModel: ProductViewModel) {
    // ViewModel or other logic to retrieve product details based on productId
    // For now, let's assume you have a method to retrieve product details based on productId
    viewModel.getProductDetails(productId)
    // Observe changes in product details
    val product = viewModel.productDetails.value

    // Render UI based on product details
    product?.let { product ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Product image
            DownloadedImage(url = product.productImage )

            Spacer(modifier = Modifier.height(16.dp))

            // Product title
            Text(
                text = product.productName,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product price
            Text(
                text = "Price: ${product.productPrice}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product description
            Text(
                text = product.productDescription,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
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

@Composable
fun ImageViewWithGlide(imageUrl: String) {
    val context = LocalContext.current
    AndroidView(factory = { context ->
        ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }) { imageView ->
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }
}