package fr.ugovignon.foodlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.ExitToApp
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DrawerComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF824083))
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = fr.ugovignon.foodlist.R.drawable.logo_licorne),
            contentDescription = "logo-licorne",
            modifier = Modifier
                .clip(CircleShape)
                .size(320.dp)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD2A8D3)),
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFD2A8D3))
        ) {
            Text(text = "Import")
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD2A8D3)),
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFD2A8D3))
        ) {
            Text(text = "Export")
        }

    }
}