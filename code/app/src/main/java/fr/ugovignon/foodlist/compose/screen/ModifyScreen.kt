package fr.ugovignon.foodlist.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.compose.LazyColumnModifyIngredients
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Product

@Composable
fun ModifyScreen(
    navController: NavHostController,
    product: Product,
    modifyViewModel: ModifyViewModel,
    mainViewModel: MainViewModel
) {

    val openDialogAdd = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2A8D3))
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
                        if(modifyViewModel.validationModification(product, mainViewModel)) {
                            navController.popBackStack()
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    bitmap = product.bitmap!!.asImageBitmap(),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
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
            LazyColumnModifyIngredients(product.getIngredients(), modifyViewModel, mainViewModel, openDialogAdd)
        }
    }
}