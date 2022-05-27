package com.example.plutus

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            var selected : Category = Category(1,"1")

            val list = ArrayList<Category>()
            for(i in 1..10){
                list.add(Category(i.toLong(),i.toString()))
            }
            val transactions = ArrayList<Transaction>()
            for(i in 1..10){
                transactions.add(Transaction(i,"Transaction n°$i","28/11/1998",5,5,1))
            }
            val navController: NavHostController = rememberNavController()

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
                                categories = list,
                                transactions = transactions,
                                onCategorySelected = {e -> selected =e},
                                navController = navController
                            )
                        }
                        composable("transaction/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toInt()
                            if(id != null){
                                Log.i(ContentValues.TAG,"id: ${id-1}")
                                ShowTransaction(transactions[id-1])
                            }}

                    }
                }
            }
        }
    }
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
    Button(modifier = Modifier.size(10.dp).height(10.dp), onClick = {
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
    categories: List<Category>,
    transactions : List<Transaction>,
    onCategorySelected: (Category) -> Unit,
    navController: NavController,
) {
    val selectedCategory = remember { mutableStateOf(Category(1,"1")) }
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("upload") }
    val fabShape = RoundedCornerShape(50)




    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {result.value = "FAB clicked"},
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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f)

            HomeAppBar(
                backgroundColor = appBarColor,
            )

            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory.value,
                onCategorySelected = {e -> selectedCategory.value = e},
            )

            TransactionGrid(transactions,navController)

//            CategoryPayment(
//                modifier = Modifier.fillMaxSize()
//            )
        }
    }
}

@Composable
private fun HomeAppBar(
    backgroundColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = "Salut",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton( onClick = {} ) {
                Icon(imageVector = Icons.Filled.Search, "search")
            }
            IconButton( onClick = {} ) {
                Icon(imageVector = Icons.Filled.AccountCircle, "acount")
            }
        }
    )
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
fun TransactionGrid(transactions: List<Transaction>,navController: NavController) {
    var infoSelect: Int by remember {
        mutableStateOf(0)
    }

    LazyVerticalGrid(
        cells = GridCells.Fixed(1)
    ) {

        items(transactions.size) { index ->

            var transaction = transactions[index];

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable {  navController.navigate("transaction/${transaction.id}") },
                elevation = 10.dp

            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(text = "${transaction.title}",
                        style = MaterialTheme.typography.body1,)
                    Text(text = "${transaction.price} €",
                        style = MaterialTheme.typography.body1,)
                }
            }
        }
    }
}

@Composable
fun ShowTransaction(transaction:Transaction){
    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        Text(text = "${transaction.title}",
            style = MaterialTheme.typography.body1,)
        Text(text = "${transaction.price} €",
            style = MaterialTheme.typography.body1,)
    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}