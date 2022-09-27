package fr.ugovignon.foodlist.compose

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.codekidlabs.storagechooser.StorageChooser
import fr.ugovignon.foodlist.MainActivity
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.data.parsingData
import fr.ugovignon.foodlist.helpers.getBitmapFromString
import fr.ugovignon.foodlist.helpers.getStringFromBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

@Composable
fun DrawerComposable(
    context: Context,
    mainViewModel: MainViewModel,
    httpClient: OkHttpClient,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF824083))
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_licorne),
            contentDescription = "logo-licorne",
            modifier = Modifier
                .clip(CircleShape)
                .size(320.dp)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_pink)),
            onClick = {
                showFilePicker(
                    context,
                    mainViewModel,
                    httpClient,
                    scope,
                    scaffoldState
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "Importer des produits")
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_pink)),
            onClick = {
                showDirectoryPicker(
                    context,
                    mainViewModel,
                    scope,
                    scaffoldState
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "Exporter des produits")
        }

    }
}

fun showFilePicker(
    context: Context,
    mainViewModel: MainViewModel,
    httpClient: OkHttpClient,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val formats = arrayListOf("txt")

    val chooser = StorageChooser.Builder()
        .withActivity(context as MainActivity)
        .withFragmentManager(context.fragmentManager)
        .withMemoryBar(true)
        .allowCustomPath(true)
        .customFilter(formats)
        .setType(StorageChooser.FILE_PICKER)
        .build()

    chooser.setOnSelectListener { path ->
        readFile(context, mainViewModel, path, httpClient, scope)
        scope.launch { scaffoldState.drawerState.close() }
    }

    chooser.show()
}

fun showDirectoryPicker(
    context: Context,
    mainViewModel: MainViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val chooser = StorageChooser.Builder()
        .withActivity(context as MainActivity)
        .withFragmentManager(context.fragmentManager)
        .withMemoryBar(true)
        .allowCustomPath(true)
        .setType(StorageChooser.DIRECTORY_CHOOSER)
        .build()

    chooser.setOnSelectListener { path ->
        writeFile(mainViewModel, path)
        scope.launch { scaffoldState.drawerState.close() }
    }

    chooser.show()
}

fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
    if (context != null && permissions != null) {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
    }
    return true
}

fun writeFile(mainViewModel: MainViewModel, directotyPath: String) {
    val directory = File(directotyPath, "FoodList")
    directory.mkdirs()
    val file = File(directory, "data.txt")

    file.bufferedWriter().use { out ->
        val jsonArray = JSONArray()
        mainViewModel.productManager.getList().forEach { product ->
            val jsonObject = JSONObject()
            val jsonArrayIngredients = JSONArray()
            jsonObject.put("code", product.code)
            jsonObject.put("name", product.name)
            jsonObject.put("image", getStringFromBitmap(product.bitmap))

            product.getIngredients().forEach {
                val ingredient = JSONObject()
                ingredient.put("name", it.name)

                jsonArrayIngredients.put(ingredient)
            }
            jsonObject.put("ingredients", jsonArrayIngredients)
            jsonArray.put(jsonObject)
        }
        out.write(jsonArray.toString())
    }
}

fun readFile(
    context: Context,
    mainViewModel: MainViewModel,
    filePath: String,
    httpClient: OkHttpClient,
    coroutineScope: CoroutineScope
) {
    mainViewModel.productManager.clear()
    val dataText = File(filePath).readText(Charsets.UTF_8)
    try {
        val jsonArray = JSONArray(dataText)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val code = jsonObject.getString("code")
            val name = jsonObject.getString("name")
            val image = getBitmapFromString(jsonObject.getString("image"))
            val ingredients = mutableListOf<Ingredient>()

            val jsonArrayIngredients = jsonObject.getJSONArray("ingredients")
            for (j in 0 until jsonArrayIngredients.length()) {
                val jsonObjectIngredient = jsonArrayIngredients.getJSONObject(j)
                ingredients.add(Ingredient(jsonObjectIngredient.getString("name")))
            }

            val product = Product(code, name, ingredients, image)
            mainViewModel.productManager.add(product)
            mainViewModel.addFilters(product.getIngredients())
            coroutineScope.launch {
                mainViewModel.dataStoreProductManager.saveProduct(product, context, mainViewModel)
            }
        }
    } catch (e: JSONException) {
        e.printStackTrace();
    }
}