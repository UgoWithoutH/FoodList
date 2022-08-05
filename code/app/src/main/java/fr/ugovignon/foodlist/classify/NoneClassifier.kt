package fr.ugovignon.foodlist.classify

import androidx.compose.runtime.snapshots.SnapshotStateList
import fr.ugovignon.foodlist.data.Product

class NoneClassifier(name: String) : Classifier(name) {
    override fun classify(list: MutableList<Product>) {
        //Nothing
    }
}