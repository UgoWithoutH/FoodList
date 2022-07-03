package fr.ugovignon.foodlist.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

class ProductList{

    private val list: SnapshotStateList<Product> = mutableStateListOf()

    fun add(product: Product){
        list.add(product)
    }

    fun remove(product: Product){
        list.remove(product)
    }

    fun getList(searchText: String) : SnapshotStateList<Product>{
        var searchTextLower = searchText.lowercase()
        var listProduct = list.toMutableStateList()
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
}