package com.ist.ondemand.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ist.ondemand.R
import com.ist.ondemand.common.Routes
import com.ist.ondemand.presentation.common.navigateTo

/**
 * Enum class representing the items in the bottom navigation bar.
 *
 * @property icon The resource ID of the icon associated with the item.
 * @property navDestination The navigation destination associated with the item.
 */
enum class BottomNavigationItem(val icon: Int, val navDestination: Routes) {
   SERVICES(R.drawable.ic_home, Routes.Services),
    SEARCH(R.drawable.ic_search, Routes.Search),
    PROFILE(R.drawable.ic_posts,Routes.Profile),
}


@Composable
fun BottomNavigationMenu(selectedItem: BottomNavigationItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(top = 10.dp)
            .background(color = Color(0xFF3E2723), shape = RoundedCornerShape(2.dp))
    )    {
        /**
         * Renders the bottom navigation items as a row of images.
         *
         * @param navController The navigation controller used for navigating to different destinations.
         * @param selectedItem The currently selected bottom navigation item.
         */
        for (item in BottomNavigationItem.values()) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .weight(1f)
                    .padding(top = 13.dp)// Adjust the vertical padding as needed
                    .clickable {
                        navigateTo(navController, item.navDestination)
                    },
                colorFilter = if (item == selectedItem) ColorFilter.tint(color = Color(0xFFF5E1CB))
                else ColorFilter.tint(color = Color(0xFF945C22))
            )
        }
    }
}