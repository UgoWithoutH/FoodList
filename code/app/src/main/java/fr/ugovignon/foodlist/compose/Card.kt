package fr.ugovignon.foodlist

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
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
import fr.ugovignon.foodlist.helpers.resizeBitmap
import fr.ugovignon.foodlist.managers.ProductManager
import kotlinx.coroutines.launch

@Composable
fun CardComposable(
    navController: NavHostController,
    product: Product,
    mainViewModel: MainViewModel,
    setShowDialog: (Boolean) -> Unit
) {
    val displayedImageSize = 150

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
                    bitmap = resizeBitmap(product.bitmap!!, displayedImageSize).asImageBitmap(),
                    product.name,
                    modifier = Modifier
                        .padding(start = paddingDeDans, bottom = paddingDeDans, end = paddingDeDans)
                        .size(displayedImageSize.dp)
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
                    modifier = Modifier
                        .constrainAs(delete) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                    onClick = {
                        mainViewModel.productToDelete = product
                        setShowDialog(true)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = Color.Black,
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