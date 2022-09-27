package fr.ugovignon.foodlist.compose.navigation

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.compose.screen.*
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.managers.ProductManager
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import okhttp3.OkHttpClient

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    httpClient: OkHttpClient,
    barcodeLauncher: ActivityResultLauncher<ScanOptions>,
    productManager: ProductManager,
    searchTextState: String,
    mainViewModel: MainViewModel,
    modifyViewModel: ModifyViewModel,
    addViewModel: AddViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    )
    {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController, productManager, barcodeLauncher, mainViewModel)
        }

        composable(route = Screen.DetailScreen.route) {
            val result =
                navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
            if (result != null) {
                DetailScreen(navController, result, modifyViewModel)
            }
        }

        composable(route = Screen.ModifyScreen.route) {
            val result =
                navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
            if (result != null) {
                ModifyScreen(navController, result, modifyViewModel, mainViewModel)
            }
        }

        composable(route = Screen.AddScreen.route) {
            AddScreen(navController, mainViewModel, addViewModel)
        }
    }
}