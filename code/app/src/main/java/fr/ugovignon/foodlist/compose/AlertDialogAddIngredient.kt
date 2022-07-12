package fr.ugovignon.foodlist.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Ingredient
import fr.ugovignon.foodlist.data.Product

@Composable
fun AlertAddDialogIngredientComposable(
    openDialog: MutableState<Boolean>,
    product: Product,
    snapshotStateList: SnapshotStateList<Ingredient>,
    modifyViewModel: ModifyViewModel
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            modifier = Modifier
                .padding(16.dp),
            text = {
                OutlinedTextField(
                    value = modifyViewModel.addIngredientName,
                    onValueChange = {
                        modifyViewModel.addIngredientName = it
                    },
                    label = { Text(text = "nom ingrédient") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF4F4E4F),
                        focusedLabelColor = Color(0xFF4F4E4F),
                        unfocusedBorderColor = Color(0xFF7C7B7C),
                        unfocusedLabelColor = Color(0xFF7C7B7C),
                        cursorColor = Color(0xFF4F4E4F),
                        textColor = Color.Black
                    )
                )
            },
            buttons = {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, bottom = 16.dp),
                ) {
                    val (
                        cancel,
                        valid
                    ) = createRefs()

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF824083)),
                        onClick = { openDialog.value = false },
                        modifier = Modifier
                            .constrainAs(cancel) {
                            start.linkTo(parent.start)
                        }
                    ) {
                        Text(
                            text = "Annuler",
                            color = Color.White
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF824083)),
                        modifier = Modifier.constrainAs(valid) {
                            end.linkTo(parent.end)
                        },
                        onClick = {
                            if (modifyViewModel.addIngredientName.isNotBlank()) {
                                product.addIngredient(Ingredient(modifyViewModel.addIngredientName))
                                snapshotStateList.clear()
                                snapshotStateList.addAll(product.getIngredient())
                            }
                            openDialog.value = false
                        }) {
                        Text(
                            text = "Valider",
                            color = Color.White
                        )
                    }
                }
            },
            backgroundColor = Color(0xFFD2A8D3)
        )
    }
}


/*
(
    openDialog: MutableState<Boolean>,
    product: Product,
    snapshotStateList: SnapshotStateList<Ingredient>,
    modifyViewModel: ModifyViewModel
) {
    if (openDialog.value) {
        val (innerPaddingStart, innerPaddingEnd, innerPaddingBottom) = 30

        Dialog(
            onDismissRequest = { openDialog.value = false },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(
                            start = innerPaddingStart.dp,
                            end = innerPaddingEnd.dp,
                            bottom = innerPaddingBottom.dp
                        )
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val cancel = createRef()

                        IconButton(
                            modifier = Modifier
                                .constrainAs(cancel) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                },
                            onClick = {
                                openDialog.value = !openDialog.value
                            }) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Annuler",
                                tint = Color.Red
                            )
                        }
                    }

                    OutlinedTextField(
                        value = modifyViewModel.addIngredientName,
                        onValueChange = {
                            modifyViewModel.addIngredientName = it
                        },
                        label = { Text(text = "nom ingrédient") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = innerPaddingStart.dp, end = innerPaddingEnd.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                if (modifyViewModel.addIngredientName.isNotBlank()) {
                                    product.addIngredient(Ingredient(modifyViewModel.addIngredientName))
                                    snapshotStateList.clear()
                                    snapshotStateList.addAll(product.getIngredient())
                                }
                                openDialog.value = false
                            }) {
                            Text(text = "Valider")
                        }
                    }
                }
            }
        )
    }
}
 */