package com.example.plutus

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Category
import com.example.plutus.core.classes.Transaction
import com.example.plutus.ui.theme.PlutusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selected  = Category(1,"1")

            val navController: NavHostController = rememberNavController()

            val viewModel: TransactionViewModel = viewModel()
            val viewState by viewModel.state.collectAsState()
/*
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.insert("new Trans", "05-27-2022", 400, 1, 1, 1);
            }*/
            PlutusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android" )
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {

                        composable(route = "home") {
                            HomeContent(
                                selectedCategory2 = selected,

                                onCategorySelected = {e -> selected =e},
                                navController = navController
                            )
                        }
                        composable(route = "addTransaction") {
                            addingTransaction(navController = navController)
                        }
                        composable("transaction/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toInt()
                            if(id != null){
                                Log.i(ContentValues.TAG,"id: ${id-1}")
                                ShowTransaction(viewState.transactions.get(id-1).transaction,navController)
                            }}

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun addingTransaction(viewModel: TransactionViewModel = viewModel() , navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Adding transaction") },
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                } else {
                    null
                }

            )
        },
        content = {
            var text by remember { mutableStateOf(TextFieldValue("")) }

            var price by remember { mutableStateOf(TextFieldValue("")) }
            //keyboard stuff
            val (focusRequester) = FocusRequester.createRefs()
            val keyboardController = LocalSoftwareKeyboardController.current

            var accepteTransaction  by remember { mutableStateOf(false) }

            Column(modifier = Modifier.padding(50.dp)) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { newText ->
                        price = newText
                        accepteTransaction = price.text.isNotEmpty() && text.text.isNotEmpty()
                    },keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester.requestFocus() }
                    ),
                    label = { Text(text = "Price") },

                    )
                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        accepteTransaction = price.text.isNotEmpty() && text.text.isNotEmpty()
                    },
                    label = { Text(text = "Title") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide()
                            //accepteTransaction = true
                        }
                    ),
                    modifier = Modifier.focusRequester(focusRequester),
                    )

                Button(modifier = Modifier.padding(20.dp),
                    enabled = accepteTransaction
                    ,onClick = {
                        if(text.text.isNotEmpty()){
                            CoroutineScope(Dispatchers.IO).launch {
                            viewModel.insert(title = text.text,"today", price.text.toInt(),5,1,7);
                            }
                            navController.navigate("home")
                        }
                    }) {
                    Text(text = "ADD")
                }
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(name: String, viewModel: TransactionViewModel = viewModel() ) {

    val viewState by viewModel.state.collectAsState()



    if (!viewState.transactions.isEmpty()) {
        LazyColumn {
            items(viewState.transactions) { item ->
                Card(
                    modifier = Modifier.padding(4.dp),
                    backgroundColor = Color.LightGray
                ) {
                    Text(
                        text = item.transaction.title,
                        modifier = Modifier.padding(24.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                }
            }
           
        }
    }
    Button(modifier = Modifier
        .size(10.dp)
        .height(10.dp), onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.insert(title = "ThinkPad","today", 80,5,3,7);

        }
    }) {
        Text(text = "ADD")
    }





}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlutusTheme {
        Greeting("Android")
    }
}


@Composable
fun HomeContent(
    selectedCategory2: Category,
    onCategorySelected: (Category) -> Unit,
    navController: NavController,
) {
    val selectedCategory = remember { mutableStateOf(Category(1,"1")) }
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("upload") }
    val fabShape = RoundedCornerShape(50)




    val categories = ArrayList<Category>()
    for(i in 1..10){
        categories.add(Category(i.toLong(),i.toString()))
    }

    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("addTransaction")},
                shape = fabShape,
                backgroundColor = Color(0xFFFF8C00)
            ) {
                Icon(Icons.Filled.Add,"")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar(
                cutoutShape = fabShape,
                content = {
                    BottomNavigation() {
                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Home , "")
                            },
                            label = { Text(text = "Home")},
                            selected = selectedItem.value == "Home",
                            onClick = {
                                result.value = "Favorite icon clicked"
                                selectedItem.value = "Home"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Info ,  "")
                            },


                            label = { Text(text = "Info")},
                            selected = selectedItem.value == "Info",
                            onClick = {
                                result.value = "Upload icon clicked"
                                selectedItem.value = "Info"
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(160.dp)) {
                HeaderTransactions()
            }
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.DarkGray,
                modifier = Modifier.fillMaxSize().padding(top = 70.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    CategoryTabs(
                        categories = categories,
                        selectedCategory = selectedCategory.value,
                        onCategorySelected = {e -> selectedCategory.value = e},
                    )

                    TransactionGrid(navController)


                }
            }

        }
    }
}




@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 24.dp,
        indicator = emptyTabIndicator,
        modifier = Modifier.fillMaxWidth(),
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) }
            ) {
                ChoiceChipContent(
                    text = category.title,
                    selected = index == selectedIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.secondary.copy(alpha = 0.87f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> Color.Black
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionGrid(navController: NavController, viewModel: TransactionViewModel = viewModel()) {
    var infoSelect: Int by remember {
        mutableStateOf(0)
    }
    val viewState by viewModel.state.collectAsState()

    if (!viewState.transactions.isEmpty()) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(1)
        ) {

            items(viewState.transactions) { item ->

                item.transaction.title;
                val textColor = if (item.transaction.price <0) Color.Red else Color.Green
                Card(
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = Color.White,
                    modifier = Modifier.padding(10.dp).width(180.dp)
                        .clickable { navController.navigate("transaction/${item.transaction.id}") },
                    elevation = 10.dp

                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(
                            text = "${item.transaction.title}",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "${item.transaction.price} €",
                            style =  TextStyle(
                                color = textColor,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTransaction(transaction:Transaction ,navController: NavController ){

    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("upload") }

    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        bottomBar = {
            BottomAppBar(
                content = {
                    BottomNavigation() {
                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Home , "")
                            },
                            label = { Text(text = "All Transaction")},
                            selected = selectedItem.value == "all Transaction",
                            onClick = {
                               navController.navigate("home")
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Info ,  "")
                            },

                            label = { Text(text = "Rien")},
                            selected = selectedItem.value == "RIEN",
                            onClick = {
                                result.value = "Upload icon clicked"
                                selectedItem.value = "Info"
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            )
        }
    ) {



       /* Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "${transaction.title}",
                style = MaterialTheme.typography.body1,
            )
            Text(
                 text = "${transaction.price} €",
                style = MaterialTheme.typography.body1,
            )
        }*/
        TransactionLayout(transaction)
    }
}

@Composable
fun TransactionLayout(transaction:Transaction){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(240.dp)) {
                HeaderView()
            }
            Card(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                backgroundColor = Color.DarkGray,
                modifier = Modifier.fillMaxSize().padding(top = 70.dp)

            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(30.dp)
                ) {


                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 10.dp, bottom = 20.dp),
                        text = transaction.title,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp),
                        text = "${transaction.price} €",
                        textAlign = TextAlign.Center,
                        fontSize = 60.sp
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp),
                        text = "${transaction.date}",
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp
                    )

                }
            }
        }

}

@Composable
fun HeaderView() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Image(
            modifier = Modifier.wrapContentWidth(),
            bitmap = ImageBitmap.imageResource(id = R.drawable.wallet),
            contentDescription ="Wallet"
        )
    }
}

@Composable
fun HeaderTransactions() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 20.dp, top=60.dp)
    ) {


        Text(
            text = "Total Balance",
            color = Color.White,
            style = TextStyle(
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
        )
        Text(
            text = "13.790€",
            color = Color.White,
            style = TextStyle(
                fontSize = 42.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight(500)
            )
        )
    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}