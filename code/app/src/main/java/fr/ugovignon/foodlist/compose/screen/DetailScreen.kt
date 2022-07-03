package fr.ugovignon.foodlist.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.compose.LazyColumnIngredients
import fr.ugovignon.foodlist.data.Product

@Composable
fun DetailScreen(navController: NavHostController, product: Product){
    val size = 20

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.name,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 35.sp
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                bitmap = product.bitmap!!.asImageBitmap(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(350.dp)
                    .clip(RoundedCornerShape(100.dp))
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Ingrédients",
            style = TextStyle(textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold),
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(25.dp))
        if(product.ingredients!!.isNotEmpty()){
            LazyColumnIngredients(product.ingredients, size)
        }
    }
}