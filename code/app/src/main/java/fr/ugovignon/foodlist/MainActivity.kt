package fr.ugovignon.foodlist

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.compose.ScaffoldComposable
import fr.ugovignon.foodlist.compose.hasPermissions
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.parsingData
import fr.ugovignon.foodlist.managers.DataStoreProductManager
import fr.ugovignon.foodlist.ui.theme.FoodListTheme
import kotlinx.coroutines.launch
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
                        mainViewModel.productManager.add(product)
                        mainViewModel.addFilters(product.getIngredients())
                        lifecycleScope.launch {
                            mainViewModel.dataStoreProductManager.saveProduct(product, getContext(), mainViewModel)
                        }
                    }
                }
            })
        }
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val modifyViewModel: ModifyViewModel by viewModels()
    private val addViewModel: AddViewModel by viewModels()

    private fun getContext() : Context{
        return this
    }

    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
            mainViewModel.dataStoreProductManager = DataStoreProductManager()

            lifecycleScope.launchWhenStarted {
                mainViewModel.productManager.addAll(mainViewModel.dataStoreProductManager.getProducts(getContext(), mainViewModel))
            }
        }
        else{
            mainViewModel.dataStoreProductManager = savedInstanceState.getParcelable<DataStoreProductManager>("dataStore")!!
        }

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0)
        }

        setContent {
            FoodListTheme {
                val navController = rememberNavController()
                ScaffoldComposable(
                    navController = navController,
                    productManager = mainViewModel.productManager,
                    barcodeLauncher = barcodeLauncher,
                    httpClient = client,
                    mainViewModel = mainViewModel,
                    modifyViewModel = modifyViewModel,
                    addViewModel = addViewModel,
                    context = this
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("dataStore", mainViewModel.dataStoreProductManager)
    }
}