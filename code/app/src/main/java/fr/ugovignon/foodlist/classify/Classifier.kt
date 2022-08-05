package fr.ugovignon.foodlist.classify

import androidx.compose.runtime.snapshots.SnapshotStateList
import fr.ugovignon.foodlist.data.Product

abstract class Classifier(val name: String) {
    abstract fun classify(list: MutableList<Product>)
}