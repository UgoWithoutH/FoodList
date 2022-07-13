package fr.ugovignon.foodlist.compose.screen

import android.content.Intent
import android.content.res.Configuration
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.data.ProductList

@Composable
fun AddScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2A8D3)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = addViewModel.title,
                onValueChange = {
                    addViewModel.title = it
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
            Button(
                onClick = {

                }
            ) {
                if (addViewModel.image == null) {
                    Text(text = "Sélectionner une image")
                } else {
                    Image(
                        bitmap = addViewModel.image!!.asImageBitmap(),
                        contentDescription = "image produit"
                    )
                }
            }
        }
    }
}
