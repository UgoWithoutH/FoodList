package fr.ugovignon.foodlist.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(val name: String, val ingredients: List<String>?, val bitmap: Bitmap?) : Parcelable

/*var product1 = Product("pain", null, )
var product2 = Product("lait", null, null)
var product3 = Product("kinder", null, null)
var product4 = Product("jambon", null, null)
var product5 = Product("blé", null, null)
var product6 = Product("miel", null, null)*/
