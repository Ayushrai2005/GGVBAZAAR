package ggv.ayush.myapplication.Repositories

import com.google.firebase.firestore.FirebaseFirestore
import ggv.ayush.myapplication.DrawerScreens.Product
import kotlinx.coroutines.tasks.await

class ProductRepository() {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getProductDetails(productId: String): Product {
        return try {
            val documentSnapshot = firestore.collection("Products").document(productId).get().await()
            if (documentSnapshot.exists()) {
                val product = documentSnapshot.toObject(Product::class.java)
                product ?: throw Exception("Failed to parse product details")
            } else {
                throw Exception("Product not found")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching product details: ${e.message}")
        }
    }
}
