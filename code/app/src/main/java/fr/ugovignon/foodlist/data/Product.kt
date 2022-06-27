package fr.ugovignon.foodlist.data

data class Product(val nom: String, val ingredients: List<String>?, val imageURL: String?)

var product1 = Product("pain", null, null)
var product2 = Product("lait", null, null)
var product3 = Product("kinder", null, null)
var product4 = Product("jambon", null, null)
var product5 = Product("blé", null, null)
var product6 = Product("miel", null, null)
