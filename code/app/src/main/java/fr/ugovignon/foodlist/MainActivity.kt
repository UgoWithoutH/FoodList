package fr.ugovignon.foodlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.data.parsingData
import fr.ugovignon.foodlist.data.stubListOfProduct
import fr.ugovignon.foodlist.ui.theme.FoodListTheme
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val client = OkHttpClient()

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.getContents() == null) {
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

                        var product = parsingData(response.body!!.string())
                        productList.add(product)
                    }
                }
            })
        }
    }

    private val productList = stubListOfProduct()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodListTheme {
                ScaffoldComposable()
            }
        }
    }

    @Composable
    fun ScaffoldComposable(){
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = { DrawerComposable() },
            topBar = {
                TopAppBar(
                    title = { Text(text = "Food List") },
                    backgroundColor = MaterialTheme.colors.background,
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu,
                            "menu")
                        }
                    }
                )
            },
            content = {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ){
                    val(
                        topbar,
                        column,
                        fab,
                        fab2
                    ) = createRefs()
                    LazyColumnComposable(feedItems = productList.getList())
                    FloatingActionButton(
                        onClick = { barcodeLauncher.launch(ScanOptions()) },
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
        )
    }
}