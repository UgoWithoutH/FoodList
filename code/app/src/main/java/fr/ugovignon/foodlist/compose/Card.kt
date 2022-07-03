package fr.ugovignon.foodlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.compose.screen.Screen

@Composable
fun CardComposable(navController: NavHostController, product: Product){
    val paddingDeHors = 16.dp
    val paddingDeDans = 8.dp
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(paddingDeHors)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "product",
                    value = product
                )
                navController.navigate(Screen.DetailScreen.route)
            }
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
                ){
            Row(
                modifier = Modifier.padding(paddingDeDans),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Image(
                    bitmap = product.bitmap!!.asImageBitmap(),
                    product.name,
                    modifier = Modifier
                        .padding(start = paddingDeDans, bottom = paddingDeDans, end = paddingDeDans)
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                )
            }
            Text(
                text = product.name,
                style = MaterialTheme.typography.h5
            )
        }
    }
}