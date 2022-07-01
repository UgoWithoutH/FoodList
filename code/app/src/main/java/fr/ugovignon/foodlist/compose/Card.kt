package fr.ugovignon.foodlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.ugovignon.foodlist.data.Product

@Composable
fun CardComposable(product: Product){
    val paddingDeHors = 16.dp
    val paddingDeDans = 8.dp
    val openDialog = remember { mutableStateOf(false) }
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(paddingDeHors)
            .clickable(onClick = { openDialog.value = !openDialog.value })
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
                    product.nom,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = paddingDeDans, bottom = paddingDeDans, end = paddingDeDans)
                        .height(50.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                )
            }
            Text(
                text = product.nom,
                style = MaterialTheme.typography.h5,
                color = Color.Blue
            )
        }
    }
}