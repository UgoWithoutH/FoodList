package fr.ugovignon.foodlist.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(var name: String) : Parcelable {
}