package fr.ugovignon.foodlist.compose.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.CustomDialogPictureComposable
import fr.ugovignon.foodlist.compose.LazyColumnModifyIngredients
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Product
import fr.ugovignon.foodlist.helpers.resizeBitmap
import kotlinx.coroutines.launch

@Composable
fun ModifyScreen(
    navController: NavHostController,
    product: Product,
    modifyViewModel: ModifyViewModel,
    mainViewModel: MainViewModel
) {
    val displayedImageSize = 300
    val openDialogAdd = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.custom_pink))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier.padding(top = 15.dp, end = 15.dp, bottom = 20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        if (modifyViewModel.validationModification(product, mainViewModel)) {
                            navController.popBackStack()
                            scope.launch {
                                mainViewModel.dataStoreProductManager.saveProductModified(
                                    product,
                                    modifyViewModel.olderProduct!!,
                                    context,
                                    mainViewModel
                                )
                            }
                        }
                    }
                ) {
                    Text("valider")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = modifyViewModel.name,
                    onValueChange = {
                        modifyViewModel.name = it
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
            }
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                bitmap = resizeBitmap(product.bitmap!!, displayedImageSize).asImageBitmap(),
                contentDescription = product.name,
                modifier = Modifier
                    .size(displayedImageSize.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ingrédients",
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    fontSize = 25.sp
                )
                IconButton(
                    onClick = {
                        openDialogAdd.value = !openDialogAdd.value
                    }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "ajouter ingrédient",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            LazyColumnModifyIngredients(
                product.getIngredients(),
                modifyViewModel,
                mainViewModel,
                openDialogAdd,
                modifyViewModel.ingredients.toMutableStateList()
            )
        }
    }
}