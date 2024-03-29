package fr.ugovignon.foodlist.compose.screen

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.CardComposable
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.CircularIndeterminateProgressBar
import fr.ugovignon.foodlist.compose.CustomDialogDeleteProduct
import fr.ugovignon.foodlist.compose.CustomDialogModifyIngredient
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.managers.ProductManager

@Composable
fun MainScreen(
    navController: NavHostController,
    productManager: ProductManager,
    barcodeLauncher: ActivityResultLauncher<ScanOptions>,
    mainViewModel: MainViewModel
) {

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        CustomDialogDeleteProduct(
            {
                showDialog.value = it
            },
            mainViewModel
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2A8D3))
    ) {
        val (
            select,
            productColumn,
            fab,
            fab2
        ) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(select) {
                    top.linkTo(parent.top)
                }
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.custom_mauve))
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            mainViewModel.expandedSort = !mainViewModel.expandedSort
                        }
                        .fillMaxWidth(0.45f)
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row() {
                            Text(text = "classer par ")
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        Text(text = mainViewModel.currentValueClassifier.name)
                    }


                    DropdownMenu(
                        expanded = mainViewModel.expandedSort,
                        onDismissRequest = {
                            mainViewModel.expandedSort = false
                        }) {

                        mainViewModel.getListClassify().forEach {

                            DropdownMenuItem(
                                onClick = {
                                    mainViewModel.currentValueClassifier = it
                                    mainViewModel.expandedSort = false
                                }) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = it.name)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.custom_mauve))
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            mainViewModel.expandedFilter = !mainViewModel.expandedFilter
                        }
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                        .background(colorResource(R.color.custom_mauve)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row() {
                            Text(text = "filtrer par ")
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        Text(text = mainViewModel.currentValueFilter.name)
                    }
                    DropdownMenu(
                        expanded = mainViewModel.expandedFilter,
                        onDismissRequest = {
                            mainViewModel.expandedFilter = false
                        }) {

                        mainViewModel.getListFilter().forEach {

                            DropdownMenuItem(
                                onClick = {
                                    mainViewModel.currentValueFilter = it
                                    mainViewModel.expandedFilter = false
                                }) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = it.name)
                                }
                            }
                        }
                    }
                }
            }
        }

        CircularIndeterminateProgressBar(mainViewModel.loading.value, mainViewModel.loadingMessage.value)
        
        LazyColumn(
            modifier = Modifier
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 80.dp)
                .background(Color.Transparent)
                .constrainAs(productColumn) {
                    top.linkTo(select.bottom)
                }
        ) {
            items(productManager.getList(mainViewModel.searchTextState, mainViewModel.currentValueClassifier, mainViewModel.currentValueFilter)) { item ->
                if(mainViewModel.loading.value){
                    mainViewModel.loading.value = false
                }
                CardComposable(
                    navController,
                    item,
                    mainViewModel
                ) {
                    showDialog.value = it
                }
            }
        }

        FloatingActionButton(
            onClick = {
                val options = ScanOptions().setBeepEnabled(false)
                barcodeLauncher.launch(options)
            },
            backgroundColor = colorResource(R.color.custom_mauve),
            modifier = Modifier
                .constrainAs(fab) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "ajout produit",
                tint = Color.White
            )
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddScreen.route)
            },
            backgroundColor = colorResource(R.color.custom_mauve),
            modifier = Modifier.constrainAs(fab2) {
                bottom.linkTo(fab.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = "scan produit",
                tint = Color.White
            )
        }
    }
}