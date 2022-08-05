package fr.ugovignon.foodlist.compose.view_models

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.ugovignon.foodlist.data.Ingredient

class AddViewModel : ViewModel() {

    var name by mutableStateOf("")

    var image by mutableStateOf<Bitmap?>(null)

    var ingredients = mutableListOf<Ingredient>()

    var ingredientTextToAdd by mutableStateOf("")

    var ingredientTextToRename by mutableStateOf("")

    var ingredientSelected: Ingredient? = null

    fun addCurrentIngredient(): Boolean {
        val ingredient = Ingredient(ingredientTextToAdd)

        if (ingredientTextToAdd.isNotEmpty()
            &&
            ingredientTextToAdd.isNotBlank()
            &&
            !ingredients.contains(ingredient)
        ) {
            ingredientTextToAdd = ""
            return ingredients.add(ingredient)
        }
        return false
    }

    fun removeIngredientSelected() {
        ingredients.remove(ingredientSelected)
    }

    fun renameIngredientSelected(): Boolean {
        val ingredientTmp = Ingredient(ingredientTextToRename)

        if (ingredientTextToRename.isNotBlank()
            &&
            ingredientTextToRename.isNotEmpty()
            &&
            ingredientTextToRename != ingredientSelected!!.name
            &&
            !ingredients.contains(ingredientTmp)
        ) {
            ingredientSelected?.name = ingredientTextToRename
            return true
        }

        return false
    }
}