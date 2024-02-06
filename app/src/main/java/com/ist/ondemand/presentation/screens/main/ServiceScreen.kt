package com.ist.ondemand.presentation.screens.main

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.ist.ondemand.presentation.MainViewModel

// Added CoffeeItem data class and coffee list
data class CoffeeItem(
    val name: String,
    val amount: String,
)

val coffees = listOf(
    CoffeeItem("Espresso", "$2.99"),
    CoffeeItem("Latte", "$3.49"),
    CoffeeItem("Cappuccino", "$3.99"),
    CoffeeItem("Mocha", "$4.49"),
    CoffeeItem("Americano", "$2.49"),
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServiceScreen(navController: NavController, vm: MainViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var filteredCoffees by remember { mutableStateOf(coffees) }

    val isSearchIconVisible by rememberUpdatedState(newValue = searchQuery.isEmpty())

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isSearchActive = true
                    }
            ) {
                // Updated BasicTextField to TextField
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        filteredCoffees = coffees.filter { coffee ->
                            coffee.name.contains(searchQuery, ignoreCase = true)
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
                        .border(
                            width = 2.dp,
                            color = Color(0xFF3E2723), // Coffee brown color
                            shape = RoundedCornerShape(33.dp)
                        )
                        .padding(8.dp)
                        .size(30.dp),
                )

                // Add search icon inside the search area
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp),
                    colorFilter = ColorFilter.tint(color = Color(0xFF3E2723))
                )

            }

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
        Text("Categories", style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E2723)
        ))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
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
        Spacer(modifier = Modifier.height(18.dp))
        Text("Results:")
        Spacer(modifier = Modifier.height(10.dp))

        if (filteredCoffees.isEmpty()) {
            Text("No results found.")
        } else {
            filteredCoffees.forEach { coffee ->
                Surface(
                    elevation = 8.dp, // Set the elevation here
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            // Image at the top
                            Image(
                                painter = painterResource(R.drawable.coffee_image),
                                contentDescription = "Coffee Image",
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.background)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Name of the coffee
                            Text(coffee.name, style = MaterialTheme.typography.h6)

                            Spacer(modifier = Modifier.height(4.dp))

                            // Amount of the coffee
                            Text("Amount: ${coffee.amount}", style = MaterialTheme.typography.body2)

                            Spacer(modifier = Modifier.weight(1f))

                            // "+" button to add to the cart
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { /* Handle button click */ },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add to Cart")
                                        Text("Add to Cart")
                                    }
                                }
                            }
                        }
                    }
                }
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
    val backgroundColor = if (isSelected) Color(0xFF3E2723) else Color.Gray
    val textColor = if (isSelected) Color.White else Color(0xFF3E2723)

    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable { onClick.invoke() }
    ) {
        Text(
            text = category,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(
                    horizontal = 17.dp,
                    vertical = 10.dp,
                )
        )
    }
}
