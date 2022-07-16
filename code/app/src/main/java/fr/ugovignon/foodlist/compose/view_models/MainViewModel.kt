package fr.ugovignon.foodlist.compose.view_models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import fr.ugovignon.foodlist.Constants
import fr.ugovignon.foodlist.classify.AlphabeticalClassifier
import fr.ugovignon.foodlist.classify.Classifier
import fr.ugovignon.foodlist.classify.NoneClassifier
import fr.ugovignon.foodlist.compose.search.SearchWidgetState
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.managers.DataStoreProductManager
import fr.ugovignon.foodlist.managers.ProductManager

class MainViewModel : ViewModel() {

    //Search
    var searchWidgetState by mutableStateOf(SearchWidgetState.CLOSED)
    var searchTextState by mutableStateOf("")

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        searchWidgetState = newValue
    }

    fun updateSearchTextState(newValue: String) {
        searchTextState = newValue
    }

    //Select box
    private val listClassify = listOf(NoneClassifier("Rien"), AlphabeticalClassifier("Alphabétique"))
    var expandedSort by mutableStateOf(false)
    var currentValueClassifier by mutableStateOf(listClassify[0])

    private val listFilter = mutableListOf(Ingredient(Constants.NONE_FILTER))
    var expandedFilter by mutableStateOf(false)
    var currentValueFilter by mutableStateOf(listFilter[0])

    fun getListClassify(): List<Classifier>{
        return listClassify.toList()
    }

    fun getListFilter(): List<Ingredient>{
        return listFilter.toList()
    }

    fun removeFilter(filter: Ingredient){
        listFilter.remove(filter)
    }

    fun addFilter(filter: Ingredient){
        if(!listFilter.contains(filter)){
            listFilter.add(filter)
        }
    }

    fun addFilters(ingredients: List<Ingredient>){
        ingredients.forEach {
            addFilter(it)
        }
    }


    //Product list
    val productManager = ProductManager()

    fun checkIngredientFilter(ingredient: Ingredient){
        productManager.getList().forEach {
            if(it.getIngredients().contains(ingredient)){
                return
            }
        }

        removeFilter(ingredient)
    }

    //datastore
    lateinit var dataStoreProductManager: DataStoreProductManager
}