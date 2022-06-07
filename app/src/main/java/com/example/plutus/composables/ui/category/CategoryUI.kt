package com.example.plutus.composables.ui.category

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.plutus.core.classes.Category

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}
@SuppressLint("UnrememberedMutableState")
@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val allCategory = Category(Int.MAX_VALUE.toLong(), "all")
    var allSelected = mutableStateOf(false);
    val mutableList = mutableListOf<Category>();
    mutableList.add(allCategory);
    mutableList.addAll(categories)
    val selectedIndex = mutableList.indexOfFirst { it.id == selectedCategory.id }

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 24.dp,
        indicator = emptyTabIndicator,
        modifier = Modifier.fillMaxWidth(),
    ) {
        mutableList.forEachIndexed { index, category ->

            Tab(
                selected = index == selectedIndex,
                onClick = {
                    onCategorySelected(category)
                }
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