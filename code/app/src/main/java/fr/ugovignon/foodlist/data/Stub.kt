package fr.ugovignon.foodlist.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

fun stubListOfProduct() : ProductList{
    var productList = ProductList()
    productList.add(product1)
    productList.add(product2)
    productList.add(product3)
    productList.add(product4)
    productList.add(product5)
    productList.add(product6)

    return productList
}