package fr.ugovignon.foodlist.compose.screen

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.CardComposable
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.ProductList

@Composable
fun MainScreen(
    navController: NavHostController,
    productList: ProductList,
    barcodeLauncher: ActivityResultLauncher<ScanOptions>,
    mainViewModel: MainViewModel
) {

    val selectPaddingExt = 5
    val selectPaddingInt = 8

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
                    .background(Color(0xFF824083))
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
                    .background(Color(0xFF824083))
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            mainViewModel.expandedFilter = !mainViewModel.expandedFilter
                        }
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                        .background(Color(0xFF824083)),
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

        LazyColumn(
            modifier = Modifier
                .background(Color.Transparent)
                .constrainAs(productColumn) {
                    top.linkTo(select.bottom)
                }
        ) {
            items(productList.getList(mainViewModel.searchTextState, mainViewModel.currentValueClassifier, mainViewModel.currentValueFilter)) { item ->
                CardComposable(navController, item, productList)
            }
        }

        FloatingActionButton(
            onClick = {
                val options = ScanOptions().setBeepEnabled(false)
                barcodeLauncher.launch(options)
            },
            backgroundColor = Color(0xFF824083),
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
            backgroundColor = Color(0xFF824083),
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