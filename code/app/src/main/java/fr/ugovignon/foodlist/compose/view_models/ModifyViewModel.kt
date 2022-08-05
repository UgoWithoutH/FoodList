package fr.ugovignon.foodlist.compose.view_models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import fr.ugovignon.foodlist.compose.search.SearchWidgetState
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.data.Product


class ModifyViewModel : ViewModel() {

    var name by mutableStateOf("")

    var ingredientSelected: Ingredient? = null

    var modifyIngredientName by mutableStateOf("")

    var addIngredientName by mutableStateOf("")

    var ingredients = mutableListOf<Ingredient>()

    var ingredientsRemoved = mutableListOf<Ingredient>()

    var ingredientAdded = mutableListOf<Ingredient>()

    fun initModel(_name: String, _ingredients: List<Ingredient>){
        name = _name
        ingredients = _ingredients.toMutableList()
    }

    fun addIngredient(ingredient: Ingredient){
        if(!ingredients.contains(ingredient)){
            ingredientAdded.add(ingredient)
            ingredients.add(ingredient)
        }
    }

    fun removeIngredientSelected(){
        ingredientsRemoved.add(ingredientSelected!!)
        ingredients.remove(ingredientSelected)
    }

    fun validationModification(product: Product, mainViewModel: MainViewModel) : Boolean{
        if(name.isNotBlank() && name.isNotEmpty()){
            product.name = name
            product.changeIngredientsList(ingredients)
            chechFilters(mainViewModel)

            return true
        }

        return false
    }
    
    private fun chechFilters(mainViewModel: MainViewModel){
        ingredientAdded.forEach { ingredient ->
            mainViewModel.addFilter(ingredient)
        }
        
        ingredientsRemoved.forEach { ingredient ->
            mainViewModel.checkIngredientFilter(ingredient)
        }
    }
}