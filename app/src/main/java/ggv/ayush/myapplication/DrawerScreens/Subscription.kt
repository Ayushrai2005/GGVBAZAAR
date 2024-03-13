package ggv.ayush.myapplication.DrawerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


// Dummy data representing an order item
data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Double
)

// Dummy list of order items
val dummyOrderItems = listOf(
    OrderItem("Item 1", 2, 10.99),
    OrderItem("Item 2", 1, 5.99),
    OrderItem("Item 3", 3, 8.49)
)

@Composable
fun Subscription() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Your Order",
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.h5
            )

            // Display order items
            dummyOrderItems.forEachIndexed { index, orderItem ->
                OrderItemRow(orderItem = orderItem)
                if (index < dummyOrderItems.size - 1) {
                    Divider()
                }
            }

            // Display total section
            TotalSection()
        }
    }

    @Composable
    fun OrderItemRow(orderItem: OrderItem) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = orderItem.name)
            Text(text = "${orderItem.quantity} x ${orderItem.price}")
        }
    }

    @Composable
    fun TotalSection() {
        var total = 0.0
        dummyOrderItems.forEach { orderItem ->
            total += orderItem.quantity * orderItem.price
        }
        Button(
            onClick = { /* Handle order confirmation */ },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(text = "Total: $${String.format("%.2f", total)}")
        }
    }

