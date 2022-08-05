package fr.ugovignon.foodlist.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.data.Ingredient

@Composable
fun AlertModifyDialogIngredientComposable(
    openDialog: MutableState<Boolean>,
    snapshotStateList: SnapshotStateList<Ingredient>,
    modifyViewModel: ModifyViewModel,
    mainViewModel: MainViewModel
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFD2A8D3)),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF824083)),
                        onClick = {
                            modifyViewModel.removeIngredientSelected()
                            //mainViewModel.checkIngredientFilter(modifyViewModel.ingredient!!)
                            snapshotStateList.clear()
                            snapshotStateList.addAll(modifyViewModel.ingredients)
                            openDialog.value = false
                        }) {
                        Text(
                            text = "Supprimer",
                            color = Color.White
                        )
                    }
                }
            },
            text = {
                OutlinedTextField(
                    value = modifyViewModel.modifyIngredientName,
                    onValueChange = {
                        modifyViewModel.modifyIngredientName = it
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
                        modifier = Modifier.constrainAs(cancel) {
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
                            if (modifyViewModel.ingredientSelected!!.name != modifyViewModel.modifyIngredientName) {
                                modifyViewModel.ingredientSelected!!.name =
                                    modifyViewModel.modifyIngredientName
                                snapshotStateList.clear()
                                snapshotStateList.addAll(modifyViewModel.ingredients)
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