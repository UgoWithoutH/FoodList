package fr.ugovignon.foodlist.compose.view_models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import fr.ugovignon.foodlist.compose.search.SearchWidgetState
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.data.Product


class ModifyViewModel : ViewModel() {

    var title by mutableStateOf("")

    var ingredient: Ingredient? = null

    var modifyIngredientName by mutableStateOf("")

    var addIngredientName by mutableStateOf("")
}