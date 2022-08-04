package fr.ugovignon.foodlist

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.codekidlabs.storagechooser.StorageChooser
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.parsingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import java.io.IOException
import java.security.AccessController.getContext

@Composable
fun DrawerComposable(context: Context, mainViewModel: MainViewModel, httpClient: OkHttpClient) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF824083))
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = fr.ugovignon.foodlist.R.drawable.logo_licorne),
            contentDescription = "logo-licorne",
            modifier = Modifier
                .clip(CircleShape)
                .size(320.dp)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD2A8D3)),
            onClick = {
                showFilePicker(context, mainViewModel, httpClient, coroutineScope)
            },
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .height(50.dp)
                .background(Color(0xFFD2A8D3))
                .clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "Importer des produits")
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD2A8D3)),
            onClick = {
                showDirectoryPicker(context, mainViewModel)
            },
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .height(50.dp)
                .background(Color(0xFFD2A8D3))
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
    coroutineScope: CoroutineScope
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
        readFile(context, mainViewModel, path, httpClient, coroutineScope)
    }

    chooser.show()
}

fun showDirectoryPicker(context: Context, mainViewModel: MainViewModel) {
    val chooser = StorageChooser.Builder()
        .withActivity(context as MainActivity)
        .withFragmentManager(context.fragmentManager)
        .withMemoryBar(true)
        .allowCustomPath(true)
        .setType(StorageChooser.DIRECTORY_CHOOSER)
        .build()

    chooser.setOnSelectListener { path ->
        writeFile(mainViewModel, path)
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
        mainViewModel.productManager.getList().forEach { product ->
            out.write(product.code + "\n")
        }
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
    mainViewModel.loading.value = true
    val inputStream = File(filePath).inputStream()
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { productCode ->
            val request = Request.Builder()
                .url("https://fr.openfoodfacts.org/api/v2/product/${productCode}")
                .build()

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        var product = parsingData(response.body!!.string(), httpClient)
                        mainViewModel.productManager.add(product)
                        mainViewModel.addFilters(product.getIngredients())
                        coroutineScope.launch {
                            mainViewModel.dataStoreProductManager.saveProduct(product, context)
                        }
                    }
                }
            })
        }
        mainViewModel.loading.value = false
    }
}