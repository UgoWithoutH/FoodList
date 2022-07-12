package fr.ugovignon.foodlist.compose.navigation

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.compose.screen.*
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.data.ProductList
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import okhttp3.OkHttpClient

@Composable
fun SetUpNavGraph(navController: NavHostController,
                  httpClient: OkHttpClient,
                  barcodeLauncher: ActivityResultLauncher<ScanOptions>,
                  productList: ProductList,
                  searchTextState: String,
                  mainViewModel: MainViewModel,
                  modifyViewModel: ModifyViewModel,
                  addViewModel: AddViewModel
){

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    )
    {
        composable(route = Screen.MainScreen.route){
            MainScreen(navController, productList, barcodeLauncher, mainViewModel)
        }

        composable(route = Screen.DetailScreen.route) {
            val result = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
            if(result != null) {
                DetailScreen(navController, result, modifyViewModel)
            }
        }

        composable(route = Screen.ModifyScreen.route) {
            val result = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
            if(result != null) {
                ModifyScreen(navController, result, modifyViewModel)
            }
        }

        composable(route = Screen.AddScreen.route) {
            val result = navController.previousBackStackEntry?.savedStateHandle?.get<ProductList>("productList")
            if(result != null) {
                AddScreen(navController, result, addViewModel)
            }
        }
    }
}