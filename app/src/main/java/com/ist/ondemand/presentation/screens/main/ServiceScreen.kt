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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.draw.shadow
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
    CoffeeItem(1, "Espresso", "$2.99"),
    CoffeeItem(2, "Latte", "$3.49"),
    CoffeeItem(3, "Cappuccino", "$3.99"),
    CoffeeItem(4, "Mocha", "$4.49"),
    CoffeeItem(5, "Americano", "$2.49"),
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
                    value = if (searchQuery.isEmpty()) "What Coffee Do you Like?..." else searchQuery,
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
                        .width(250.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF3E2723), // Coffee brown color
                            shape = RoundedCornerShape(16.dp) // Adjusted corner radius
                        )
                        .padding(12.dp)
                        .height(26.dp), // Adjusted height
                    textStyle = LocalTextStyle.current.copy(
                        color = Color(0xFF3E2723),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
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
        Spacer(modifier = Modifier.height(20.dp))
        Text("Results:")
        Spacer(modifier = Modifier.height(10.dp))

        if (filteredCoffees.isEmpty()) {
            Card(
                modifier = Modifier
                    .padding(50.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Sorry No Coffee Found!!",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 20.sp, // Increase the font size to 20sp
                        fontWeight = FontWeight.Bold,
                        color = Color.Red // Change the color to black or any other color you prefer
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn{
                filteredCoffees.forEach { coffee ->
                    item {
                        Surface(
                            color = Color(0xFFF5E1CB),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .shadow(elevation = 6.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(190.dp) // Set the height
                                    .width(170.dp)  // Set the width
                                    .clickable {
                                        //handle on click
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    // Image at the top
                                    Image(
                                        painter = painterResource(R.drawable.logo),
                                        contentDescription = "Coffee Image",
                                        modifier = Modifier
                                            .height(60.dp)
                                            .height(90.dp) // Set the height
                                            .width(70.dp)  // Set the width
                                            .clip(shape = RoundedCornerShape(8.dp))
                                            .padding(bottom = 2.dp)
                                            .align(Alignment.CenterHorizontally)

                                    )


                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Name of the coffee
                                    Text(
                                        coffee.name,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 20.sp, // Increase the font size to 18sp
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF3E2723) // Change the color to black or any other color you prefer
                                        )
                                    )

                                    // Amount of the coffee
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            buildAnnotatedString {

                                                withStyle(
                                                    style = SpanStyle(
                                                        color = Color.Black,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp
                                                    ),
                                                    block = { append(coffee.amount) }
                                                )
                                            },
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        IconButton(
                                            onClick = { /* Handle button click */ },
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add to Cart",
                                                modifier = Modifier.size(28.dp), // Increase the size of the icon
                                                tint = Color.Black // Set the color of the icon to black
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }

                }
            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .bottomLink()
        ) {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.SERVICES,
                navController = navController
            )
        }


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
