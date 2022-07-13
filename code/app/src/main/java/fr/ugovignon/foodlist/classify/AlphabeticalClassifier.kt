package fr.ugovignon.foodlist.classify

import androidx.compose.runtime.snapshots.SnapshotStateList
import fr.ugovignon.foodlist.data.Product

class AlphabeticalClassifier(name: String) : Classifier(name) {
    override fun classify(list: SnapshotStateList<Product>) {
        list.sortBy { it.name }
    }
}