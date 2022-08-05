package fr.ugovignon.foodlist.data

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(val code: String, var name: String, private var ingredients: MutableList<Ingredient>, val bitmap: Bitmap?) : Parcelable{

    fun getIngredients() : List<Ingredient>{
        return ingredients.toList()
    }

    fun removeIngredient(ingredient: Ingredient){
        ingredients.remove(ingredient)
    }

    fun addIngredient(ingredient: Ingredient): Boolean{
        if(!ingredients.contains(ingredient)){
            ingredients.add(ingredient)
            return true
        }
        return false
    }

    fun isIngredientsNotEmpty() : Boolean{
        return ingredients.isNotEmpty()
    }

    fun changeIngredientsList(ingredientsList: MutableList<Ingredient>){
        ingredients = ingredientsList
    }

    override fun equals(other: Any?): Boolean {
        return (other is Product) && other.name == this.name
    }
}
