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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.data.Ingredient

@Composable
fun LazyColumnAddIngredients(
    addViewModel: AddViewModel,
    feeditems: SnapshotStateList<Ingredient>
) {
    val openDialogModify = remember { mutableStateOf(false) }

    AlertAddScreenDialogComposable(openDialogModify, feeditems, addViewModel)
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(feeditems) { item ->
            Card(
                modifier = Modifier.clickable {
                    addViewModel.ingredientSelected = item
                    addViewModel.ingredientTextToRename = item.name
                    openDialogModify.value = !openDialogModify.value
                },
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