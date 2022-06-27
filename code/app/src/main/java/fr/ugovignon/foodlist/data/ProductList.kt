package fr.ugovignon.foodlist.data

class ProductList{

    private val list: MutableList<Product> = mutableListOf()

    fun add(product: Product){
        list.add(product)
    }

    fun remove(product: Product){
        list.remove(product)
    }

    fun getList() = list.toList()
}