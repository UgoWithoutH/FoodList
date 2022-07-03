package fr.ugovignon.foodlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.compose.ScaffoldComposable
import fr.ugovignon.foodlist.compose.navigation.SetUpNavGraph
import fr.ugovignon.foodlist.compose.search.MainViewModel
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.data.parsingData
import fr.ugovignon.foodlist.data.stubListOfProduct
import fr.ugovignon.foodlist.ui.theme.FoodListTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
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
                        productList.add(product)
                    }
                }
            })
        }
    }

    private val productList = stubListOfProduct()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodListTheme {
                var navController = rememberNavController()
                ScaffoldComposable(
                    navController = navController,
                    productList = productList,
                    barcodeLauncher = barcodeLauncher,
                    httpClient = client,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}