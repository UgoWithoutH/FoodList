package fr.ugovignon.foodlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.ugovignon.foodlist.data.Product

@Composable
fun LazyColumnComposable(feedItems : List<Product>){
    LazyColumn(){
        items(feedItems){
            item -> CardComposable(product = item)  
        }
    }
}