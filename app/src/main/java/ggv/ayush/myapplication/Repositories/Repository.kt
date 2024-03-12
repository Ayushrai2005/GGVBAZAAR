package ggv.ayush.myapplication.Repositories

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ggv.ayush.myapplication.DrawerScreens.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel() : ViewModel() {
    private val repository = ProductRepository()

    val productDetails = mutableStateOf<Product?>(null)

    fun getProductDetails(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = repository.getProductDetails(productId)
                productDetails.value = product
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
