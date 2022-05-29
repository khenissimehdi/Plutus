package com.example.plutus

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plutus.core.BookletViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Category
import com.example.plutus.core.classes.Transaction
import com.example.plutus.ui.theme.PlutusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selected  = Category(1,"1")

            val navController: NavHostController = rememberNavController()

            val viewModel: TransactionViewModel = viewModel()
            val viewModelCurrentBooklet:  CurrentBookletViewModel = viewModel()
            val viewState by viewModel.state.collectAsState()
            val currentBookletState by viewModelCurrentBooklet.state.collectAsState()

            /*CoroutineScope(Dispatchers.IO).launch {
                if (currentBookletState.booklet.id.toInt() == 0) {
                    viewModelCurrentBooklet.update(1);
                    //viewModelCurrentBooklet.setCurrentBookLet()
                }
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
                            ModalBottomSheet(
                                selectedCategory2 = selected,
                                onCategorySelected = {e -> selected =e} ,
                                navController = navController,
                                content = { addingTransaction(navController = navController, currentBookletViewModel = viewModelCurrentBooklet) },
                                 currentBookletViewModel = viewModelCurrentBooklet
                            )
                        }
                        composable(route = "transactions/{id}") {
                            val id = it.arguments?.getString("id")?.toInt()
                            if(id != null) {
                               TransactionGrid(navController = navController, currentBookletViewModel = viewModelCurrentBooklet)
                            }

                        }
                        /*composable(route = "addTransaction") {

                            addingTransaction(navController = navController, bookltId = currentBookletState.bookletcurr.id.toInt())
                        }*/
                        composable(route = "updateTransaction/{id}") {
                            val id = it.arguments?.getString("id")?.toInt()
                            if(id != null) {
                                updateTransaction(navController = navController, id = id)
                            }
                        }
                        composable(route = "booklets") {


                           // BookletGrid(navController = navController)
                        }
                        composable("transaction/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toInt()
                            if(id != null){
                                ShowTransaction(viewState.transactions.get(id-1).transaction,navController)
                            }}

                    }
                }
            }
        }
    }
}
@Composable
fun updateTransaction(viewModel: TransactionViewModel = viewModel() , navController: NavController, id: Int) {
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

            Column(modifier = Modifier.padding(50.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                    },
                    label = { Text(text = "Title") },

                    )
                OutlinedTextField(
                    value = price,
                    onValueChange = { newText ->
                        price = newText
                    },keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Price") },

                    )
                Button(modifier = Modifier.padding(20.dp)
                    ,onClick = {
                        if(text.text.isNotEmpty()){
                            CoroutineScope(Dispatchers.IO).launch {
                                var transaction= viewModel.getTransactionById(id)
                                transaction.title = text.text
                                transaction.price = price.text.toInt()
                                viewModel.updateById(transaction)
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


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun addingTransaction(viewModel: TransactionViewModel = viewModel() , navController: NavController, currentBookletViewModel: CurrentBookletViewModel){
    Scaffold(
        topBar = {

        },
        content = {
            var text by remember { mutableStateOf(TextFieldValue("")) }

            var price by remember { mutableStateOf(TextFieldValue("")) }
            //keyboard stuff
            val (focusRequester) = FocusRequester.createRefs()
            val keyboardController = LocalSoftwareKeyboardController.current
            val viewState by currentBookletViewModel.state.collectAsState()
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
                            var id = viewState.bookletcurr.id
                            Log.i("koko", id.toString())
                            viewModel.insert(title = text.text,"today", price.text.toInt(),5,id.toInt(),
                                1);
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

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun ModalBottomSheet(viewModel: TransactionViewModel = viewModel(),
                     currentBookletViewModel: CurrentBookletViewModel,
                     selectedCategory2: Category,
                     onCategorySelected: (Category) -> Unit,
                     navController: NavController,
                     content: @Composable() () -> Unit,

) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    
    ModalBottomSheetLayout(
        sheetContent = {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)

            ) {
                content()
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        MainContent(
            selectedCategory2 = selectedCategory2,
            onCategorySelected =  onCategorySelected,
            navController = navController,

            onClick = {
                coroutineScope.launch {
                sheetState.animateTo(ModalBottomSheetValue.Expanded)
            }}, viewModel = currentBookletViewModel )
    }
}

@Composable
fun MainContent(selectedCategory2: Category,
                viewModel: CurrentBookletViewModel,
                onCategorySelected: (Category) -> Unit,
                navController: NavController,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    HomeContent(
        selectedCategory2 = selectedCategory2,
        onCategorySelected = onCategorySelected,
        navController = navController,
        onClick = onClick, viewModel = viewModel)

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    selectedCategory2: Category,
    onCategorySelected: (Category) -> Unit,
    viewModel: CurrentBookletViewModel,
    navController: NavController,
    onClick: () -> Unit
) {
    val selectedCategory = remember { mutableStateOf(Category(1,"1")) }
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("Home") }
    val fabShape = RoundedCornerShape(50)

    val categories = ArrayList<Category>()
    for(i in 1..10){
        categories.add(Category(i.toLong(),i.toString()))
    }

    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            
            FloatingActionButton(
                onClick =  onClick ,
                shape = fabShape,
                backgroundColor = Color(0xFFFF8C00)
            ) {
                Icon(Icons.Filled.Add,"")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
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
                                selectedItem.value = "Home"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Book  ,  "Booklets")
                            },

                            label = { Text(text = "Booklets")},
                            selected = selectedItem.value == "Booklets",
                            onClick = {
                                selectedItem.value = "Booklets"

                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



/*            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory.value,
                onCategorySelected = {e -> selectedCategory.value = e},
            )*/

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(160.dp)) {
                HeaderTransactions()
            }
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.DarkGray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)

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
                    if (selectedItem.value.equals("Home")) {
                        TransactionGrid(navController = navController, currentBookletViewModel = viewModel)
                    } else {
                        BookletGrid(navController = navController, seleted = selectedItem, currentBookletViewModel = viewModel)
                    }

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
fun TransactionGrid(navController: NavController,
                    viewModel: TransactionViewModel = viewModel(),
                    currentBookletViewModel: CurrentBookletViewModel

) {

    var infoSelect: Int by remember {
        mutableStateOf(0)
    }
    val viewState by viewModel.state.collectAsState()
    val currentBookletViewState by currentBookletViewModel.state.collectAsState()
    if (!viewState.transactions.isEmpty()) {
        var transactionByBookletId = viewState.transactions.filter { it.transaction.bookletIdT == currentBookletViewState.bookletcurr.id.toInt() }
        LazyVerticalGrid(
            cells = GridCells.Fixed(1)
        ) {

            items(transactionByBookletId) { item ->

                item.transaction.title;
                val textColor = if (item.transaction.price <0) Color.Red else Color.Green
                Card(
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(180.dp)
                        .clickable { navController.navigate("transaction/${item.transaction.id}") },
                    elevation = 10.dp

                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(
                            text = item.transaction.title,
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookletGrid(navController: NavController, seleted:  MutableState<String> , viewModel: BookletViewModel = viewModel(),  currentBookletViewModel: CurrentBookletViewModel) {
    var infoSelect: Int by remember {
        mutableStateOf(0)
    }
    val viewState by viewModel.state.collectAsState()
    val currentBookletViewState by currentBookletViewModel.state.collectAsState()

    if (!viewState.booklets.isEmpty()) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(1)
        ) {

            items(viewState.booklets) { item ->

                item.title
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable {
                            CoroutineScope(Dispatchers.IO).launch {
                            val go = currentBookletViewModel.update(item.bookletId.toLong())
                             if(go) {
                                 seleted.value = "Home"
                             }
                        }},
                    elevation = 10.dp

                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.body1,
                        )
                        Text(
                            text = item.date,
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTransaction(transaction:Transaction, navController: NavController) {

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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                ) {


                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 20.dp),
                        text = transaction.title,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp),
                        text = "${transaction.price} €",
                        textAlign = TextAlign.Center,
                        fontSize = 60.sp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
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