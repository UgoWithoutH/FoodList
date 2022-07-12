package fr.ugovignon.foodlist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Product

@Composable
fun LazyColumnModifyIngredients(product: Product,
                                modifyViewModel: ModifyViewModel,
                                openDialogAdd: MutableState<Boolean>){

    val openDialogModify = remember { mutableStateOf(false) }
    val feeditems = product.getIngredient().toMutableStateList()

    AlertAddDialogIngredientComposable(openDialogAdd, product, feeditems, modifyViewModel)
    AlertModifyDialogIngredientComposable(openDialogModify, product, feeditems, modifyViewModel)
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(feeditems){
                item ->
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = 4.dp,
                modifier = Modifier.clickable {
                    modifyViewModel.ingredient = item
                    modifyViewModel.modifyIngredientName = item.name
                    openDialogModify.value = !openDialogModify.value
                }
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