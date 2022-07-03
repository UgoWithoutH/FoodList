package fr.ugovignon.foodlist.compose.screen

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.LazyColumnProduct
import fr.ugovignon.foodlist.data.ProductList

@Composable
fun MainScreen(navController: NavHostController, productList: ProductList,
               barcodeLauncher: ActivityResultLauncher<ScanOptions>,
               searchTextState: String){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ){
        val(
            fab,
            fab2
        ) = createRefs()
        LazyColumnProduct(navController, productList.getList(searchTextState))
        FloatingActionButton(
            onClick = {
                val options = ScanOptions().setBeepEnabled(false)
                barcodeLauncher.launch(options)
            },
            modifier = Modifier.constrainAs(fab) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Icon(Icons.Filled.Add, "ajout produit")
        }

        FloatingActionButton(
            onClick = {},
            modifier = Modifier.constrainAs(fab2) {
                bottom.linkTo(fab.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Icon(Icons.Filled.Create, "scan produit")
        }
    }
}