package fr.ugovignon.foodlist.compose.screen

sealed class Screen(val route: String){
    object MainScreen: Screen("main_screen")
    object DetailScreen: Screen("detail_screen")
    object ModifyScreen: Screen("modify_screen")
    object AddScreen: Screen("add_screen")
}
