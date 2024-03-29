package fr.ugovignon.foodlist.data

import fr.ugovignon.foodlist.helpers.downloadImage
import okhttp3.*
import org.json.JSONObject
import java.lang.Exception

fun parsingData(data: String, httpClient: OkHttpClient): Product {

    val content = JSONObject(data)

    val contentProduct = content.getJSONObject("product")
    val code = contentProduct.getString("code")
    val name = contentProduct.getString("product_name").replaceFirstChar {
        it.uppercase()
    }
    var ingredients: MutableList<Ingredient>

    try {
        ingredients = parsingIngredients(contentProduct.getString("ingredients_text_fr"))
    } catch (e: Exception) {
        try {
            ingredients = parsingIngredients(contentProduct.getString("ingredients_text"))
        } catch (e: Exception) {
            ingredients = mutableListOf()
        }
    }

    val url = parsingImage(
        contentProduct
            .getJSONObject("selected_images")
            .getJSONObject("front")
            .getJSONObject("display")
    )

    val image = downloadImage(url, httpClient)

    return Product(code, name, ingredients, image)
}

private fun parsingIngredients(data: String): MutableList<Ingredient> {
    val ingredientsList = mutableListOf<Ingredient>()

    val replace1 = data.lowercase().replace("\\(([0-9])*.]*\\)".toRegex(), "")
    val replace2 = replace1.replace("_", " ")
    val replace3 = replace2.replace("  ", " ")
    val replace4 = replace3.replace("\\*\\*.*,".toRegex(), "")
    val replace5 = replace4.replace("^\\s|\\s\$".toRegex(), "")
    val replace6 = replace5.replace("(,|.)$".toRegex(), "")

    val ingredientsListTmp = replace6.split(",")

    for (ing: String in ingredientsListTmp) {
        ingredientsList.add(Ingredient(ing.replace(" $".toRegex(), "")))
    }

    return ingredientsList
}

fun parsingImage(jsonObject: JSONObject): String {
    var imageFR = false
    val dataKeys = jsonObject.keys()

    while (dataKeys.hasNext()) {
        if (dataKeys.next().lowercase() == "fr") {
            imageFR = true
        }
    }

    var data: String = if (imageFR) {
        jsonObject.getString("fr")
    } else {
        var dataTmp1 = jsonObject.toString().split(",")
        var dataTmp2 = dataTmp1[0].split("\":\"")[1]
        var replace1 = dataTmp2.replace("\\", "")
        var replace2 = replace1.replace("}", "")
        replace2.replace("\"", "")
    }

    return data
}