package com.example.plutus.composables.ui.home


import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plutus.ExportUtils
import com.example.plutus.Graph
import com.example.plutus.composables.actions.booklet.addingBooklet
import com.example.plutus.composables.actions.booklet.updateBooklet

import com.example.plutus.composables.ui.category.*

import com.example.plutus.composables.ui.booklet.BookletGrid
import com.example.plutus.composables.ui.booklet.EditBookletModal
import com.example.plutus.composables.ui.header.HeaderTransactions
import com.example.plutus.composables.ui.transaction.TransactionGrid
import com.example.plutus.core.CategoryViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Booklet
import com.example.plutus.core.classes.Category
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun ModalBottomSheet(viewModel: TransactionViewModel = viewModel(),
                     currentBookletViewModel: CurrentBookletViewModel,
                     selectedCategory2: Category,
                     onCategorySelected: (Category) -> Unit,
                     navController: NavController,
                     currentNav: MutableState<String>,
                     categoryViewModel: CategoryViewModel,
                     content: @Composable() () -> Unit,

                     ) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val bookletToEdit = remember {
        mutableStateOf(Booklet())
    }
    val editButtonClicked = remember {
        mutableStateOf(false)
    }


    ModalBottomSheetLayout(
        sheetContent = {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
            ) {

                if(currentNav.value.equals("Home") ) {
                   content()
                } else {
                    if(editButtonClicked.value) {
                        updateBooklet(navController = navController, currentBookletViewModel = currentBookletViewModel, booklet =  bookletToEdit.value)
                    }
                    if(!editButtonClicked.value) {
                        addingBooklet(navController = navController, currentBookletViewModel = currentBookletViewModel)
                    }
                }

            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        MainContent(
            selectedCategory2 = selectedCategory2,
            onCategorySelected =  onCategorySelected,
            navController = navController,
            editButtonClicked = editButtonClicked,
            bookletToEdit = bookletToEdit,
            currentNav = currentNav,
            onClick = {
                coroutineScope.launch {
                    sheetState.animateTo(ModalBottomSheetValue.Expanded)

                }}, viewModel = currentBookletViewModel, categoryViewModel = categoryViewModel )
    }
}

@Composable
fun MainContent(selectedCategory2: Category,
                viewModel: CurrentBookletViewModel,
                onCategorySelected: (Category) -> Unit,
                navController: NavController,
                editButtonClicked: MutableState<Boolean>,
                bookletToEdit: MutableState<Booklet>,
                onClick: () -> Unit,
                currentNav: MutableState<String>,
                modifier: Modifier = Modifier,
                categoryViewModel: CategoryViewModel
) {

    HomeContent(
        selectedCategory2 = selectedCategory2,
        onCategorySelected = onCategorySelected,
        navController = navController,
        currentNav = currentNav,
        editButtonClicked = editButtonClicked,
        bookletToEdit = bookletToEdit,
        onClick = onClick,
        viewModel = viewModel,
        categoryViewModel = categoryViewModel
    )

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeContent(
    selectedCategory2: Category,
    onCategorySelected: (Category) -> Unit,
    viewModel: CurrentBookletViewModel,
    currentNav: MutableState<String>,
    editButtonClicked: MutableState<Boolean>,
    bookletToEdit: MutableState<Booklet>,
    navController: NavController,
    onClick: () -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val selectedCategory = remember { mutableStateOf(Category(Int.MAX_VALUE.toLong(),"all")) }
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("Home") }
    val fabShape = RoundedCornerShape(50)
    val viewState by categoryViewModel.state.collectAsState()

    val moneyState = remember { mutableStateOf(0)}
    val openDialog = remember { mutableStateOf(false) }
    val doneExport = remember { mutableStateOf(false) }

    val writePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {

            FloatingActionButton(
                onClick =  {
                    onClick()
                    editButtonClicked.value = false
                 },
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
                            label = { Text(text = "Home") },
                            selected = selectedItem.value == "Home",
                            onClick = {
                                currentNav.value = "Home"
                                selectedItem.value = "Home"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Book  ,  "Booklets")
                            },

                            label = { Text(text = "Booklets") },
                            selected = selectedItem.value == "Booklets",
                            onClick = {
                                currentNav.value = "Booklets"
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(160.dp)
            ) {

                HeaderTransactions(moneyState)


            }
            Button(onClick = {
                if (writePermissionState.status.isGranted) {
                    ExportUtils.export(Graph.database, doneExport)

                } else {
                    openDialog.value = true
                }
            }) {
                Text(text = "Export")

            }
            if (doneExport.value) {
                CustomDialogScrollable(
                    title = "File Exported !",
                    msg = "You can find your files in the Download Folder",
                    onDismiss = {doneExport.value = false}
                )
            }

            if (openDialog.value) {
                CustomDialogScrollable(
                    title = "Give full file access",
                    msg = "To change file access go to setting look for the application name and change the settings to grant full access to files  ",
                    onDismiss = {openDialog.value = false}
                )
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
                        categories = viewState.categories,
                        selectedCategory = selectedCategory.value,
                        onCategorySelected = { e ->
                            run {
                                selectedCategory.value = e
                            }
                        },
                    )
                    if (selectedItem.value.equals("Home")) {
                        TransactionGrid(
                            navController = navController,
                            currentBookletViewModel = viewModel,
                            seletedCategory = selectedCategory.value,
                            moneyState = moneyState
                        )
                    } else {
                        BookletGrid(
                            navController = navController,
                            seleted = selectedItem,
                            editButtonClicked = editButtonClicked,
                            bookletToEdit = bookletToEdit,
                            onClick = onClick,
                            currentBookletViewModel = viewModel,
                            currentNav = currentNav,
                        )
                    }

                }
            }

        }

    }
}

@Composable
fun CustomDialogScrollable(
    title: String,
    msg: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // TITLE
                Text(text = title, style = MaterialTheme.typography.subtitle1)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .weight(weight = 1f, fill = false)
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.body2
                    )
                }

                // BUTTONS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}
