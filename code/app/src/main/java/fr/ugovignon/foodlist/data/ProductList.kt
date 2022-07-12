package fr.ugovignon.foodlist.data

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.parcelize.Parcelize

@Parcelize
class ProductList : Parcelable{

    private val list: SnapshotStateList<Product> = mutableStateListOf()

    fun add(product: Product){
        list.add(product)
    }

    fun remove(product: Product){
        list.remove(product)
    }

    fun getList(searchText: String) : SnapshotStateList<Product>{
        val searchTextLower = searchText.lowercase()
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