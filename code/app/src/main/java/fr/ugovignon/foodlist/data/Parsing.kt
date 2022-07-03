package fr.ugovignon.foodlist.data

import android.graphics.BitmapFactory
import fr.ugovignon.foodlist.downloadImage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody
import okio.ByteString.Companion.encode
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.CountDownLatch

fun parsingData(data: String, httpClient: OkHttpClient) : Product{

    var content = JSONObject(data)

    var contentProduct = content.getJSONObject("product")
    var name = contentProduct.getString("product_name")

    var ingredients = parsingIngredients(contentProduct.getString("ingredients_text"))

    var url = parsingImage(contentProduct
        .getJSONObject("selected_images")
        .getJSONObject("front")
        .getJSONObject("display"))

    var image = downloadImage(url, httpClient)

    return Product(name, ingredients, image)
}

private fun parsingIngredients(data: String) : List<String>{
    val ingredientsList = mutableListOf<String>()
    //val ingredientsListTmp = data.split(",".toRegex())
    //val media = "application/json; charset=utf-8".toMediaTypeOrNull()

    /*var allIngredientsText = jsonArray.toString()
    allIngredientsText = allIngredientsText.replace("[", "")
    allIngredientsText = allIngredientsText.replace("\"", "")
    allIngredientsText  = allIngredientsText.replace("([a-z]{2}:)".toRegex(), "")
    allIngredientsText = allIngredientsText.replace("-"," ")*/

    /*val jsonObject = JSONObject()
        .put("q", allIngredientsText)
        .put("source", "en")
        .put("target", "fr")
    val requestBody = jsonObject.toString().toRequestBody(media)

    val request = Request.Builder()
        .url("https://translate.argosopentech.com/translate")
        .post(requestBody)
        .build()

    val countDownLatch = CountDownLatch(1)
    httpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            countDownLatch.countDown();
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val replace1 = response.body!!.string()
                val replace2 = replace1.split(":")
                val replace3 = replace2[1].split("\"")
                var replace4 = replace3[1].replace(".","")
                var replace5 = String(replace4.toByteArray(), Charsets.UTF_8)
                /*var tabIngredients = replace5.split(",")
                for(ing: String in tabIngredients){
                    ingredientsList.add(ing)
                }*/
            }
        }
    })*/


        val replace1 = data.lowercase().replace("\\(([0-9])*.]*\\)".toRegex(), "")
        val replace2 = replace1.replace("_", " ")
        val replace3 = replace2.replace("  ", " ")
        val replace4 = replace3.replace("\\*\\*.*,".toRegex(), "")
        val replace5 = replace4.replace("^\\s|\\s\$".toRegex(), "")
        val replace6 = replace5.replace(",$".toRegex(), "")

        val ingredientsListTmp = replace6.split(",")

        for(ing: String in ingredientsListTmp){
            ingredientsList.add(ing.replace(" $".toRegex(),""))
        }

    return ingredientsList
}

fun parsingImage(jsonObject: JSONObject) : String{
    var imageFR = false
    val dataKeys = jsonObject.keys()

    while(dataKeys.hasNext()){
        if (dataKeys.next().lowercase() == "fr"){
            imageFR = true
        }
    }

    var data: String = if(imageFR){
        jsonObject.getString("fr")
    }
    else {
        var dataTmp1 = jsonObject.toString().split(",")
        var dataTmp2 = dataTmp1[0].split("\":\"")[1]
        var replace1 = dataTmp2.replace("\\","")
        var replace2 = replace1.replace("}","")
        replace2.replace("\"","")
    }

    return data
}