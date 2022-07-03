package fr.ugovignon.foodlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.data.Product

@Composable
fun LazyColumnProduct(navController: NavHostController, feedItems : List<Product>){
    LazyColumn(){
        items(feedItems){
            item -> CardComposable(navController, product = item)
        }
    }
}