package fr.ugovignon.foodlist.compose.view_models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import fr.ugovignon.foodlist.compose.search.SearchWidgetState
import fr.ugovignon.foodlist.data.ProductList

class MainViewModel : ViewModel() {

    //Search

    var searchWidgetState by mutableStateOf(SearchWidgetState.CLOSED)

    var searchTextState by mutableStateOf("")

    val productList = ProductList()

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        searchWidgetState = newValue
    }

    fun updateSearchTextState(newValue: String) {
        searchTextState = newValue
    }

    //Select box
    private val listSort = listOf("one", "two", "three", "four", "five")
    var expandedSort by mutableStateOf(false)
    var currentValueSort by mutableStateOf(listSort[0])

    private val listFilter = listOf("one", "two", "three", "four", "five")
    var expandedFilter by mutableStateOf(false)
    var currentValueFilter by mutableStateOf(listFilter[0])

    fun getListFilter(): List<String>{
        return listFilter.toList()
    }

    fun getListSort(): List<String>{
        return listSort.toList()
    }

}