package fr.ugovignon.foodlist.compose

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.ScanOptions
import fr.ugovignon.foodlist.R
import fr.ugovignon.foodlist.compose.navigation.SetUpNavGraph
import fr.ugovignon.foodlist.compose.view_models.MainViewModel
import fr.ugovignon.foodlist.compose.search.SearchWidgetState
import fr.ugovignon.foodlist.compose.view_models.AddViewModel
import fr.ugovignon.foodlist.compose.view_models.ModifyViewModel
import fr.ugovignon.foodlist.managers.ProductManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

@Composable
fun ScaffoldComposable(
    navController: NavHostController,
    productManager: ProductManager,
    barcodeLauncher: ActivityResultLauncher<ScanOptions>,
    httpClient: OkHttpClient,
    mainViewModel: MainViewModel,
    modifyViewModel: ModifyViewModel,
    addViewModel: AddViewModel,
    context: Context
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val searchWidgetState = mainViewModel.searchWidgetState
    val searchTextState = mainViewModel.searchTextState

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerComposable(
                context,
                mainViewModel,
                httpClient,
                scope,
                scaffoldState
            )
        },
        topBar = {
            MainAppBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    mainViewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    mainViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    Log.d("Searched Text", it)
                },
                onSearchTriggered = {
                    mainViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                },
                scope = scope,
                scaffoldState = scaffoldState
            )
        },
        content = {
            SetUpNavGraph(
                navController = navController,
                httpClient = httpClient,
                barcodeLauncher = barcodeLauncher,
                productManager = productManager,
                searchTextState = searchTextState,
                mainViewModel = mainViewModel,
                modifyViewModel = modifyViewModel,
                addViewModel = addViewModel,
                context = context
            )
        }
    )
}

@Composable
fun MainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered,
                scope = scope,
                scaffoldState = scaffoldState
            )
        }
        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Food List",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_licorne),
                    contentDescription = "logo-licorne",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(45.dp)
                        .background(Color.White)
                )
            }
        },
        backgroundColor = colorResource(R.color.custom_mauve),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { scaffoldState.drawerState.open() }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { onSearchClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search icon",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = colorResource(R.color.custom_mauve)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Rechercher ici...",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }
}