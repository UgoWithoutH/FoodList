package fr.ugovignon.foodlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.ui.theme.FoodListTheme

class MainActivity : ComponentActivity() {

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodListTheme {
                ScaffoldComposable()
            }
        }
    }

    @Composable
    fun ScaffoldComposable(){
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = { },
            topBar = {
            },
            content = {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ){
                    val(
                        topbar,
                        column,
                        fab,
                        fab2
                    ) = createRefs()

                    FloatingActionButton(
                        onClick = { barcodeLauncher.launch(ScanOptions()) },
                        modifier = Modifier.constrainAs(fab) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                    ) {
                        Icon(Icons.Filled.Add, "ajout produit")
                    }

                    FloatingActionButton(
                        onClick = {},
                        modifier = Modifier.constrainAs(fab2) {
                            bottom.linkTo(fab.top, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                    ) {
                        Icon(Icons.Filled.Create, "scan produit")
                    }
                }
            }
        )
    }
}