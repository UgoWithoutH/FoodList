package fr.ugovignon.foodlist

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.compose.screen.Screen
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.managers.ProductManager
import kotlinx.coroutines.launch

@Composable
fun CardComposable(
    navController: NavHostController,
    product: Product,
    productManager: ProductManager,
    mainViewModel: MainViewModel,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val paddingDeHors = 16.dp
    val paddingDeDans = 8.dp
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .background(Color(0xFFD2A8D3))
            .padding(paddingDeHors)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "product",
                    value = product
                )
                navController.navigate(Screen.DetailScreen.route)
            }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(paddingDeDans)
                    .fillMaxWidth()
            ) {
                val (
                    image,
                    delete
                ) = createRefs()
                Spacer(modifier = Modifier.size(16.dp))
                Image(
                    bitmap = product.bitmap!!.asImageBitmap(),
                    product.name,
                    modifier = Modifier
                        .padding(start = paddingDeDans, bottom = paddingDeDans, end = paddingDeDans)
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .constrainAs(image) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                )
                IconButton(
                    modifier = Modifier.constrainAs(delete) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                    onClick = {
                        productManager.remove(product)
                        product.getIngredients().forEach {
                            mainViewModel.checkIngredientFilter(it)
                        }
                        coroutineScope.launch {
                            mainViewModel.dataStoreProductManager.deleteProduct(context, product)
                        }
                    },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_restore_from_trash_24),
                        contentDescription = "delete"
                    )
                }
            }
            Text(
                text = product.name,
                style = MaterialTheme.typography.h5,
                color = Color.Black
            )
        }
    }
}