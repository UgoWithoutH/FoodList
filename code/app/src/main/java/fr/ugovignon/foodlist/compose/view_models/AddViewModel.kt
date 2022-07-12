package fr.ugovignon.foodlist.compose.view_models

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.ugovignon.foodlist.data.Ingredient

class AddViewModel : ViewModel() {

    var title by mutableStateOf("")

    var image by mutableStateOf<Bitmap?>(null)

    var ingredients = mutableStateListOf<Ingredient>()
}