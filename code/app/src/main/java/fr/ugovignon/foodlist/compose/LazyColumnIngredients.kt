package fr.ugovignon.foodlist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.ugovignon.foodlist.compose.screen.Screen
import fr.ugovignon.foodlist.data.Ingredient

@Composable
fun LazyColumnIngredients(feedItems : List<Ingredient>){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(feedItems){
            item ->
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = 4.dp
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = item.name
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
