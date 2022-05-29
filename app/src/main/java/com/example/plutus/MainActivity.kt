package com.example.plutus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plutus.composables.actions.transaction.addingTransaction
import com.example.plutus.composables.actions.transaction.updateTransaction
import com.example.plutus.composables.ui.HomeUI.ModalBottomSheet
import com.example.plutus.composables.ui.transaction.ShowTransaction
import com.example.plutus.composables.ui.transaction.TransactionGrid
import com.example.plutus.core.CurrentBookletViewModel
import com.example.plutus.core.TransactionViewModel
import com.example.plutus.core.classes.Category
import com.example.plutus.ui.theme.PlutusTheme


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
                        composable(route = "updateTransaction/{id}") {
                            val id = it.arguments?.getString("id")?.toInt()
                            if(id != null) {
                                updateTransaction(navController = navController, id = id)
                            }
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
