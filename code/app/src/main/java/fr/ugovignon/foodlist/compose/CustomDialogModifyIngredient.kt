package fr.ugovignon.foodlist.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Ingredient

@Composable
fun CustomDialogModifyIngredient(
    setShowDialog: (Boolean) -> Unit,
    snapshotStateList: SnapshotStateList<Ingredient>,
    modifyViewModel: ModifyViewModel,
    mainViewModel: MainViewModel
) {

    val txtFieldError = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable {
                                    modifyViewModel.removeIngredientSelected()
                                    //mainViewModel.checkIngredientFilter(modifyViewModel.ingredient!!)
                                    snapshotStateList.clear()
                                    snapshotStateList.addAll(modifyViewModel.ingredients)
                                    setShowDialog(false)
                                }
                        )

                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Modifier l'ingrédient",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) android.R.color.holo_green_light else android.R.color.holo_red_dark)
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colorResource(R.color.custom_cursor)
                        ),
                        placeholder = {
                            Text(
                                text = "Nom ingrédient",
                                color = colorResource(android.R.color.darker_gray)
                            )
                        },
                        value = modifyViewModel.modifyIngredientName,
                        onValueChange = {
                            modifyViewModel.modifyIngredientName = it
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (modifyViewModel.modifyIngredientName.isEmpty()) {
                                    txtFieldError.value = "Ce champ ne peut pas être vide"
                                    return@Button
                                }
                                if (modifyViewModel.ingredientSelected!!.name == modifyViewModel.modifyIngredientName) {
                                    txtFieldError.value = "Le nom est identique"
                                    return@Button
                                }
                                modifyViewModel.ingredientSelected!!.name = modifyViewModel.modifyIngredientName
                                snapshotStateList.clear()
                                snapshotStateList.addAll(modifyViewModel.ingredients)
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_mauve)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Valider")
                        }
                    }
                }
            }
        }
    }
}