package com.ist.ondemand.presentation.screens.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ist.ondemand.R
import com.ist.ondemand.common.Routes
import com.ist.ondemand.presentation.MainViewModel
import com.ist.ondemand.presentation.common.CommonImage
import com.ist.ondemand.presentation.common.ProgressSpinner
import com.ist.ondemand.presentation.common.navigateTo

/**
 * Composable function that represents the profile screen.
 *
 * @param navController The navigation controller used for navigating between screens.
 * @param vm The view model used for managing the profile data.
 */
@Composable
fun ProfileScreen(navController: NavController, vm: MainViewModel) {
    // Check if data is being loaded
    val isLoading = vm.inProgress.value
    if (isLoading) {
        ProgressSpinner()
    } else {
        // Retrieve user data
        val userData = vm.userData.value

        // Initialize mutable state variables for name, username, and bio
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }

        // Render the profile content
        ProfileContent(vm = vm,
            name = name,
            username = username,
            bio = bio,
            onNameChange = { name = it },
            onUsernameChange = { username = it },
            onBioChange = { bio = it },
            onSave = { vm.updateProfileData(name, username, bio) },
            onBack = { navigateTo(navController = navController, Routes.Services) },
            onLogout = {
                vm.onLogout()
                navigateTo(navController, Routes.Login)
            })
    }
}

/**
 * Composable function that represents the profile screen content.
 *
 * @param vm The view model for the screen.
 * @param name The name of the user.
 * @param username The username of the user.
 * @param bio The bio of the user.
 * @param onNameChange Callback function for name changes.
 * @param onUsernameChange Callback function for username changes.
 * @param onBioChange Callback function for bio changes.
 * @param onSave Callback function for saving changes.
 * @param onBack Callback function for navigating back.
 * @param onLogout Callback function for logging out.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    vm: MainViewModel,
    name: String,
    username: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val imageUrl = vm.userData.value?.imageUrl

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(6.dp)
            .background(
                color = Color(0xFFF5E1CB),
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back), // Replace with your actual resource ID
                contentDescription = null,
                tint = Color(0xFF3E2723),
                modifier = Modifier
                    .clickable { onBack.invoke() }
                    .border(1.dp, Color(0xFFF5E1CB), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
                    .size(33.dp)  // Adjust the size to make the button smaller
                    .wrapContentSize(Alignment.CenterStart)  // Align the content to the start (left)
                    .weight(0.1f)  // Adjust the weight to prevent stretching
            )


            Text(
                text = "Save",
                modifier = Modifier
                    .clickable { onSave.invoke() }
                    .border(1.dp, Color(0xFFF5E1CB), shape = RoundedCornerShape(8.dp))
                    .padding(10.dp)
                    .background(Color(0xFF3E2723), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }




        ProfileImage(
            imageUrl = imageUrl,
            vm = vm,
            modifier = Modifier
                .size(150.dp)  // Adjust the size of the circular image holder as needed
                .clip(CircleShape)
        )




        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Name :",
                modifier = Modifier
                    .width(100.dp)
                    .padding(start = 6.dp)
                    .padding(4.dp)
                    .scale(1f),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
            )

            TextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(2.dp, Color(0xFF3E2723), shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp), // Adjust the corner radius as needed
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                ),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Username:",
                modifier = Modifier
                    .width(100.dp)
                    .padding(start = 6.dp)
                    .padding(4.dp)
                    .scale(1f),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
            )
            TextField(
                value = username,
                onValueChange = onUsernameChange,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(2.dp, Color(0xFF3E2723), shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp), // Adjust the corner radius as needed
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Bio :",
                modifier = Modifier
                    .width(100.dp)
                    .padding(start = 6.dp)
                    .padding(4.dp)
                    .scale(1f),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
            )
            TextField(
                value = bio,
                onValueChange = onBioChange,
                singleLine = false,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(150.dp)
                    .border(2.dp, Color(0xFF3E2723), shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp), // Adjust the corner radius as needed
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Logout",
                modifier = Modifier
                    .clickable { onLogout.invoke() }
                    .border(1.dp, Color(0xFFF5E1CB), shape = RoundedCornerShape(8.dp))
                    .padding(10.dp)
                    .background(Color(0xFF3E2723), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )


        }
    }
}

/**
 * Composable function that displays the profile image and allows the user to change it.
 *
 * @param imageUrl The URL of the profile image.
 * @param vm The MainViewModel instance.
 */

@Composable
fun ProfileImage(imageUrl: String?, vm: MainViewModel, modifier: Modifier) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { vm.uploadProfileImage(uri) }
    }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { launcher.launch("image/*") },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp)
                    .border(2.dp, Color(0xFF3E2723), shape = CircleShape)
            ) {
                CommonImage(data = imageUrl, modifier = Modifier.fillMaxSize())
            }
            Text(
                text = "Change profile picture",
                fontSize = 10.sp, // Adjust the font size as needed
                color = Color(0xFF3E2723),
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        val isLoading = vm.inProgress.value
        if (isLoading) ProgressSpinner()
    }
}



