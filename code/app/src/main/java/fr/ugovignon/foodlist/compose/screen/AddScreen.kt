package fr.ugovignon.foodlist.compose.screen

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.launch

@Composable
fun AddScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel
) {

    val feeditems = addViewModel.ingredients.toMutableStateList()
    var imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        CustomDialogPictureComposable(bitmap, imageUri) {
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
                            bitmap.value,
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

            if (imageUri.value != null) {
                imageUri.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it.value)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it.value!!)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }
                }
            }

            if (bitmap.value == null) {
                Button(
                    modifier = Modifier
                        .size(150.dp),
                    onClick = {
                        showDialog.value = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF824083)),
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
            } else {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    bitmap.let {
                        val data = it.value
                        if (data != null) {
                            Image(
                                bitmap = data.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                            )
                        }
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