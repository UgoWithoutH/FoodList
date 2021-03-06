package fr.ugovignon.foodlist.managers

import android.content.Context
import android.os.Parcelable
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.getBitmapFromString
import fr.ugovignon.foodlist.getStringFromBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Parcelize
class DataStoreProductManager : Parcelable {

    object PreferencesKeys {
        val PRODUCTS = stringPreferencesKey("products")
    }

    private val Context.dataStore by preferencesDataStore("app")

    suspend fun getProducts(context: Context): List<Product> {

        val productList = mutableListOf<Product>()

        val preferences = context.dataStore.data.first()
        val result = preferences[PreferencesKeys.PRODUCTS]
        if (result != null) {
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("name")
                    val image = getBitmapFromString(jsonObject.getString("image"))
                    val ingredients = mutableListOf<Ingredient>()

                    val jsonArrayIngredients = jsonObject.getJSONArray("ingredients")
                    for (j in 0 until jsonArrayIngredients.length()) {
                        val jsonObjectIngredient = jsonArrayIngredients.getJSONObject(j)
                        ingredients.add(Ingredient(jsonObjectIngredient.getString("name")))
                    }

                    val product = Product(name, ingredients, image)
                    productList.add(product)
                }
            } catch (e: JSONException) {
                e.printStackTrace();
            }
        }

        return productList
    }

    suspend fun deleteProduct(context: Context, productToDelete: Product) {
        context.dataStore.edit { settings ->
            val result = settings[PreferencesKeys.PRODUCTS]

            if (result != null) {
                try {
                    val jsonArrayResult = JSONArray(result)
                    val newJsonArray = JSONArray()
                    for (i in 0 until jsonArrayResult.length()) {
                        val jsonObject = jsonArrayResult.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val image = getBitmapFromString(jsonObject.getString("image"))
                        val ingredients = mutableListOf<Ingredient>()

                        val jsonArrayIngredients = jsonObject.getJSONArray("ingredients")
                        for (j in 0 until jsonArrayIngredients.length()) {
                            val jsonObjectIngredient = jsonArrayIngredients.getJSONObject(j)
                            ingredients.add(Ingredient(jsonObjectIngredient.getString("name")))
                        }

                        val product = Product(name, ingredients, image)
                        if (product != productToDelete) {
                            newJsonArray.put(jsonObject)
                        }
                    }
                    settings[PreferencesKeys.PRODUCTS] = newJsonArray.toString()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    suspend fun saveProduct(product: Product, context: Context) {
        val jsonObject = JSONObject()

        try {
            val jsonArrayIngredients = JSONArray()
            jsonObject.put("name", product.name)
            jsonObject.put("image", getStringFromBitmap(product.bitmap))

            product.getIngredients().forEach {
                val ingredient = JSONObject()
                ingredient.put("name", it.name)

                jsonArrayIngredients.put(ingredient)
            }
            jsonObject.put("ingredients", jsonArrayIngredients)

            saveProduct(jsonObject, context)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private suspend fun saveProduct(jsonObject: JSONObject, context: Context) {
        context.dataStore.edit { settings ->
            val result = settings[PreferencesKeys.PRODUCTS]

            if (result != null) {
                try {
                    val jsonArrayResult = JSONArray(result)
                    jsonArrayResult.put(jsonObject)
                    settings[PreferencesKeys.PRODUCTS] = jsonArrayResult.toString()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                val jsonArrayResult = JSONArray()
                jsonArrayResult.put(jsonObject)
                settings[PreferencesKeys.PRODUCTS] = jsonArrayResult.toString()
            }
        }
    }

}