package fr.ugovignon.foodlist.data

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(var name: String, private val ingredients: MutableList<Ingredient>, val bitmap: Bitmap?) : Parcelable{

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
        return ingredients!!.isNotEmpty()
    }

    override fun equals(other: Any?): Boolean {
        return (other is Product) && other.name == this.name
    }
}



/*var product1 = Product("pain", null, )
var product2 = Product("lait", null, null)
var product3 = Product("kinder", null, null)
var product4 = Product("jambon", null, null)
var product5 = Product("blé", null, null)
var product6 = Product("miel", null, null)*/
