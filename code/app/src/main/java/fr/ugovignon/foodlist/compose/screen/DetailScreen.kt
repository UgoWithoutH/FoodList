package fr.ugovignon.foodlist.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.LazyColumnIngredients
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.helpers.resizeBitmap

@Composable
fun DetailScreen(
    navController: NavHostController,
    product: Product,
    modifyViewModel: ModifyViewModel
) {

    val displayedImageSize = 300

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.custom_pink))
    ) {
        val (
            modify
        ) = createRefs()
        IconButton(
            modifier = Modifier.constrainAs(modify) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            },
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "product",
                    value = product
                )
                modifyViewModel.initModel(product.name, product.getIngredients())
                navController.navigate(Screen.ModifyScreen.route)
            }
        ) {
            Icon(Icons.Filled.Create, "modification produit")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = product.name,
                    color = Color.Black,
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
                    bitmap = resizeBitmap(product.bitmap!!, displayedImageSize).asImageBitmap(),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(displayedImageSize.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Ingrédients",
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(25.dp))
            if (product.isIngredientsNotEmpty()) {
                LazyColumnIngredients(product.getIngredients())
            }
        }
    }
}