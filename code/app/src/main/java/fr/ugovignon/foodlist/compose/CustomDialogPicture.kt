package fr.ugovignon.foodlist.compose

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import fr.ugovignon.foodlist.R

@Composable
fun CustomDialogPictureComposable(
    bitmap: MutableState<Bitmap?>,
    imageUri: MutableState<Uri?>,
    setShowDialog: (Boolean) -> Unit
) {

    val launcherCamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        bitmap.value = it
        setShowDialog(false)
    }

    val launcherGallery =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri.value = uri
            setShowDialog(false)
        }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 70.dp,
                    top = 10.dp
                ),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable {
                                    setShowDialog(false)
                                }
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
                            text = "Choix de l'image",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 35.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.size(140.dp, 100.dp),
                            border = BorderStroke(2.dp, colorResource(R.color.custom_mauve)),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_pink)),
                            onClick = {
                                launcherGallery.launch("image/*")
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(60.dp),
                                    imageVector = Icons.Filled.Image,
                                    contentDescription = "image produit",
                                    tint = Color.Black
                                )
                                Text(
                                    text = "Galerie photos",
                                    color = Color.Black
                                )
                            }
                        }
                        Button(
                            modifier = Modifier.size(140.dp, 100.dp),
                            border = BorderStroke(2.dp, colorResource(R.color.custom_mauve)),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.custom_pink)),
                            onClick = {
                                launcherCamera.launch()
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(60.dp),
                                    imageVector = Icons.Filled.CameraAlt,
                                    contentDescription = "image produit",
                                    tint = Color.Black
                                )
                                Text(
                                    text = "Caméra",
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}