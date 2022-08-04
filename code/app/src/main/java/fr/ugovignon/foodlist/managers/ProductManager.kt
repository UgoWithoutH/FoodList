package fr.ugovignon.foodlist.managers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import fr.ugovignon.foodlist.Constants
import fr.ugovignon.foodlist.classify.Classifier
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.data.Product

class ProductManager(){

    private val productList: SnapshotStateList<Product> = mutableStateListOf()

    fun add(product: Product){
        productList.add(product)
    }

    fun addAll(list: List<Product>){
        productList.addAll(list)
    }

    fun remove(product: Product){
        productList.remove(product)
    }

    fun clear(){
        productList.clear()
    }

    fun getList() : SnapshotStateList<Product>{
        return productList.toMutableStateList()
    }

    fun getList(searchText: String, classifierSelected: Classifier, filterSelected: Ingredient) : SnapshotStateList<Product>{
        val searchTextLower = searchText.lowercase()
        var listProduct = productList.toMutableStateList()

        listProduct = listFiltered(listProduct, filterSelected)
        listClassified(listProduct, classifierSelected)

        if(searchText != ""){
            listProduct = mutableStateListOf()
            for (product: Product in productList){
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
                             filterSelected: Ingredient
    ) : SnapshotStateList<Product>{
        if(filterSelected.name == Constants.NONE_FILTER) return list

        return list.filter {
            it.getIngredients().contains(filterSelected)
        }.toMutableStateList()
    }
}