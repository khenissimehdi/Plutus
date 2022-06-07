package com.example.plutus.composables.actions.transaction

import UIDatePicker
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plutus.BitMapUtils
import com.example.plutus.composables.ui.dropDownSelect.DropDownInput
import com.example.plutus.core.CategoryViewModel
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


/**
 * add Transaction
 * */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun addingTransaction(viewModel: TransactionViewModel = viewModel(), navController: NavController, currentBookletViewModel: CurrentBookletViewModel, categModel: CategoryViewModel){
    Scaffold(
        topBar = {

        },
        content = {
            var text by remember { mutableStateOf(TextFieldValue("")) }
            var date = remember { mutableStateOf(Date()) }
            var price by remember { mutableStateOf(TextFieldValue("")) }
            //keyboard stuff
            val (focusRequester) = FocusRequester.createRefs()
            val keyboardController = LocalSoftwareKeyboardController.current
            val viewState by currentBookletViewModel.state.collectAsState()
            val categViewState by categModel.state.collectAsState()
            if (viewState.bookletcurr!!.id.toInt()  == -1) {
               var icon =  BitMapUtils.getIcon(LocalContext.current, "addbooklet")
                Column( modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    if (icon != null) {
                        Image(bitmap = icon.asImageBitmap(), contentDescription = "not found")
                    }
                    Text(text = "You have to add a booklet !")
                }

                return@Scaffold
            }

            
            val seleted = remember {
                mutableStateOf(0)
            }

            val seletedString = remember {
                mutableStateOf("")
            }

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
                DropDownInput(seleted,seletedString, categViewState.categories.map { e -> e.title })
                UIDatePicker(dateState = date)


                Button(modifier = Modifier.padding(20.dp),
                    enabled = accepteTransaction
                    ,onClick = {
                        if(text.text.isNotEmpty()){
                            CoroutineScope(Dispatchers.IO).launch {
                                val id = viewState.bookletcurr!!.id
                                val ids =  categViewState.categories.map { e -> e.id }
                                Log.i("sting", seletedString.value)
                                if(!ids.contains(seleted.value.toLong())) {
                                    val idCateg = categModel.insert(seletedString.value)
                                    viewModel.insert(title = text.text,date.value, price.text.toInt(),5,id.toInt(),
                                        idCateg.toInt());

                                } else {
                                    viewModel.insert(title = text.text,date.value, price.text.toInt(),5,id.toInt(),
                                        seleted.value);

                                }
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

/**
 * update Transaction
 * */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun updateTransaction(viewModel: TransactionViewModel = viewModel(), navController: NavController, currentBookletViewModel: CurrentBookletViewModel, categModel: CategoryViewModel,transaction: Transaction) {
    Scaffold(
        topBar = {

        },
        content = {
            var text by remember { mutableStateOf(TextFieldValue(transaction.title)) }
            var date = remember { mutableStateOf(transaction.date) }
            var price by remember { mutableStateOf(TextFieldValue(transaction.price.toString())) }
            //keyboard stuff
            val (focusRequester) = FocusRequester.createRefs()
            val keyboardController = LocalSoftwareKeyboardController.current
            val viewState by currentBookletViewModel.state.collectAsState()
            val categViewState by categModel.state.collectAsState()

            val seleted = remember {
                mutableStateOf(transaction.actionIdT)
            }

            val seletedString = remember {
                mutableStateOf("")
            }

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
                DropDownInput(seleted,seletedString, categViewState.categories.map { e -> e.title })
                UIDatePicker(dateState = date)


                Button(modifier = Modifier.padding(20.dp),
                    enabled = accepteTransaction
                    ,onClick = {
                        if(text.text.isNotEmpty()){
                            CoroutineScope(Dispatchers.IO).launch {
                                val id = viewState.bookletcurr!!.id
                                val ids =  categViewState.categories.map { e -> e.id }
                                Log.i("sting", seletedString.value)
                                if(!ids.contains(seleted.value.toLong())) {
                                    val idCateg = categModel.insert(seletedString.value)
                                    transaction.date = date.value;
                                    transaction.price = price.text.toInt()
                                    transaction.title = text.text
                                    transaction.bookletIdT = id.toInt();

                                    viewModel.updateByTrans(transaction, idCateg.toInt())
                                } else {

                                    transaction.date = date.value;
                                    transaction.price = price.text.toInt()
                                    transaction.title = text.text
                                    transaction.bookletIdT = id.toInt();
                                    viewModel.updateByTrans(transaction, seleted.value)
                                }
                            }
                            navController.navigate("home")
                        }
                    }) {
                    Text(text = "EDIT")
                }
            }
        }
    )
}
