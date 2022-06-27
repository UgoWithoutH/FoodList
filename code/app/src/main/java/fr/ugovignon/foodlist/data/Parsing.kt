package fr.ugovignon.foodlist.data

import org.json.JSONObject

fun parsingData(data: String) : Product{

    var content = JSONObject(data)

    var contentProduct = content.getJSONObject("product")
    var name = contentProduct.getString("product_name_fr")

    var ingredients = parsingIngredients(contentProduct.getString("ingredients_text_fr"))

    var image = contentProduct
        .getJSONObject("selected_images")
        .getJSONObject("front")
        .getJSONObject("display")
        .getString("fr")

    return Product(name, ingredients, image)
}

private fun parsingIngredients(data: String) : List<String>{
    var ingredientsListTmp = data.split(",").toMutableList()
    var ingredientsList = mutableListOf<String>()

    for(ingredient: String in ingredientsListTmp){
        var replace1 = ingredient.replace("_", " ")
        var replace2 = replace1.replace("  ", " ")

        ingredientsList.add(replace2)
    }
    return ingredientsList
}