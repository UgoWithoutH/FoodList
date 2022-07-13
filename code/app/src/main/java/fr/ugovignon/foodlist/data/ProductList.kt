package fr.ugovignon.foodlist.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import fr.ugovignon.foodlist.Constants
import fr.ugovignon.foodlist.classify.Classifier

class ProductList(){

    private val list: SnapshotStateList<Product> = mutableStateListOf()

    fun add(product: Product){
        list.add(product)
    }

    fun remove(product: Product){
        list.remove(product)
    }

    fun getList(searchText: String, classifierSelected: Classifier, filterSelected: Ingredient) : SnapshotStateList<Product>{
        val searchTextLower = searchText.lowercase()
        var listProduct = list.toMutableStateList()

        listProduct = listFiltered(listProduct, filterSelected)
        listClassified(listProduct, classifierSelected)

        if(searchText != ""){
            listProduct = mutableStateListOf()
            for (product: Product in list){
                if(product.name.lowercase().contains(searchTextLower)){
                    listProduct.add(product)
                }
            }
        }

        return listProduct
    }

    private fun listClassified(list: SnapshotStateList<Product>,
                               classifierSelected: Classifier){
        classifierSelected.classify(list)
    }

    private fun listFiltered(list: SnapshotStateList<Product>,
                             filterSelected: Ingredient) : SnapshotStateList<Product>{
        if(filterSelected.name == Constants.NONE_FILTER) return list

        return list.filter {
            it.getIngredients().contains(filterSelected)
        }.toMutableStateList()
    }
}