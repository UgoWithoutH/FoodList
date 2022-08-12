package fr.ugovignon.foodlist.compose.screen

import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.Constants
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.CustomDialogPictureComposable
import fr.ugovignon.foodlist.compose.LazyColumnAddIngredients
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.helpers.getBitmapFromUri
import fr.ugovignon.foodlist.helpers.resizeBitmap
import kotlinx.coroutines.launch

@Composable
fun AddScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel
) {

    val displayedImageSize = 300
    val feeditems = addViewModel.ingredients.toMutableStateList()
    val hasImage = remember {
        mutableStateOf(false)
    }

    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        CustomDialogPictureComposable(hasImage, bitmap) {
            showDialog.value = it
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.custom_pink)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            ) {
                val (
                    cancel,
                    validate
                ) = createRefs()

                Button(
                    modifier = Modifier.constrainAs(cancel) {
                        start.linkTo(parent.start)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("annuler")
                }
                Button(
                    modifier = Modifier.constrainAs(validate) {
                        end.linkTo(parent.end)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        val product = Product(
                            Constants.CUSTOM_CODE,
                            addViewModel.name.replaceFirstChar {
                                it.uppercase()
                            },
                            addViewModel.ingredients,
                            resizeBitmap(bitmap.value!!, 300)
                        )
                        if (mainViewModel.productManager.add(product)) {
                            mainViewModel.addFilters(product.getIngredients())
                            scope.launch {
                                mainViewModel.dataStoreProductManager.saveProduct(product, context)
                            }
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("valider")
                }
            }

            OutlinedTextField(
                value = addViewModel.name,
                onValueChange = {
                    addViewModel.name = it
                },
                label = { Text(text = "nom du produit") },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4F4E4F),
                    focusedLabelColor = Color(0xFF4F4E4F),
                    unfocusedBorderColor = Color(0xFF7C7B7C),
                    unfocusedLabelColor = Color(0xFF7C7B7C),
                    cursorColor = Color(0xFF4F4E4F),
                    textColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(50.dp))

            if (hasImage.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = resizeBitmap(bitmap.value!!, displayedImageSize).asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(displayedImageSize.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        onClick = { showDialog.value = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_mauve)),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 10.dp
                        )
                    ) {
                     Text(text = "Sélectionner une autre image")
                    }
                }
            } else {
                Button(
                    modifier = Modifier
                        .size(150.dp),
                    onClick = {
                        showDialog.value = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_mauve)),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 10.dp
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "image produit",
                            tint = Color.White
                        )
                        Text(
                            text = "Sélectionner une image",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = addViewModel.ingredientTextToAdd,
                    onValueChange = {
                        addViewModel.ingredientTextToAdd = it
                    },
                    label = { Text(text = "ajouter un ingrédient") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF4F4E4F),
                        focusedLabelColor = Color(0xFF4F4E4F),
                        unfocusedBorderColor = Color(0xFF7C7B7C),
                        unfocusedLabelColor = Color(0xFF7C7B7C),
                        cursorColor = Color(0xFF4F4E4F),
                        textColor = Color.Black
                    )
                )
                IconButton(
                    onClick = {
                        if (addViewModel.addCurrentIngredient()) {
                            feeditems.clear()
                            feeditems.addAll(addViewModel.ingredients)
                        }
                    }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "ajouter ingrédient",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            LazyColumnAddIngredients(addViewModel, feeditems)
        }
    }
}