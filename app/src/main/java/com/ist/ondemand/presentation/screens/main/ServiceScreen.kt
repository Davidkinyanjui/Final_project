package com.ist.ondemand.presentation.screens.main

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ist.ondemand.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServiceScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val coffees = listOf("Espresso", "Latte", "Cappuccino", "Mocha", "Americano")
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var filteredCoffees by remember { mutableStateOf(coffees) }

    val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        currentTime < 12 -> "Morning"
        currentTime < 17 -> "Afternoon"
        else -> "Evening"
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5E1CB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                        block = {
                            append("Enjoy your\n\n$greeting ")
                        }
                    )
                    withStyle(
                        style = SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3E2723)),
                        block = {
                            append("Coffee!!!")
                        }
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = null,
                tint = Color(0xFF3E2723),
                modifier = Modifier
                    .size(40.dp)
                    .offset(y = 8.dp)
            )
        }

        // Integrate the coffee search functionality
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    filteredCoffees = coffees.filter { coffee ->
                        coffee.contains(searchQuery, ignoreCase = true)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    isSearchActive = false
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isSearchActive = true
                    }
            )

            if (isSearchActive && searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            searchQuery = ""
                            filteredCoffees = coffees
                        }
                        .size(18.dp)
                )
            }
        }

        // Filter buttons for Categories
        Spacer(modifier = Modifier.height(16.dp))
        Text("Categories", style = MaterialTheme.typography.subtitle1)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            CoffeeCategoryFilterButton(
                category = "All",
                selectedCategory = selectedCategory,
                onClick = { selectedCategory = "All" }
            )
            CoffeeCategoryFilterButton(
                category = "Espresso",
                selectedCategory = selectedCategory,
                onClick = { selectedCategory = "Espresso" }
            )
            CoffeeCategoryFilterButton(
                category = "Latte",
                selectedCategory = selectedCategory,
                onClick = { selectedCategory = "Latte" }
            )
            CoffeeCategoryFilterButton(
                category = "Cappuccino",
                selectedCategory = selectedCategory,
                onClick = { selectedCategory = "Cappuccino" }
            )
        }

        // Display filtered coffees
        Spacer(modifier = Modifier.height(16.dp))
        Text("Results:")
        Spacer(modifier = Modifier.height(8.dp))

        if (filteredCoffees.isEmpty()) {
            Text("No results found.")
        } else {
            filteredCoffees.forEach { coffee ->
                Text(coffee)
            }
        }

        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.SERVICES,
            navController = navController
        )
    }
}

@Composable
fun CoffeeCategoryFilterButton(category: String, selectedCategory: String, onClick: () -> Unit) {
    val isSelected = category == selectedCategory
    val backgroundColor = if (isSelected) Color(0xFF8B4513) else Color.Gray
    val textColor = if (isSelected) Color.White else Color(0xFF8B4513)

    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable { onClick.invoke() }
    ) {
        Text(
            text = category,
            color = textColor,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

