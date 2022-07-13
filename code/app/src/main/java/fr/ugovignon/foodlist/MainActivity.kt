package fr.ugovignon.foodlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.compose.ScaffoldComposable
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.parsingData
import fr.ugovignon.foodlist.ui.theme.FoodListTheme
import okhttp3.*
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val client = OkHttpClient()

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()

            val request = Request.Builder()
                .url("https://fr.openfoodfacts.org/api/v2/product/${result.contents}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        var product = parsingData(response.body!!.string(), client)
                        mainViewModel.productList.add(product)
                        mainViewModel.addFilters(product.getIngredients())
                    }
                }
            })
        }
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val modifyViewModel: ModifyViewModel by viewModels()
    private val addViewModel: AddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodListTheme {
                val navController = rememberNavController()
                ScaffoldComposable(
                    navController = navController,
                    productList = mainViewModel.productList,
                    barcodeLauncher = barcodeLauncher,
                    httpClient = client,
                    mainViewModel = mainViewModel,
                    modifyViewModel = modifyViewModel,
                    addViewModel = addViewModel
                )
            }
        }
    }
}