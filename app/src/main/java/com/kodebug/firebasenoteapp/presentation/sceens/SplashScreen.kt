package com.kodebug.firebasenoteapp.presentation.sceens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kodebug.firebasenoteapp.R
import com.kodebug.firebasenoteapp.navigation.NavRoutes
import com.kodebug.firebasenoteapp.ui.theme.colorBlack
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    Scaffold{ innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorBlack)
        ) {


            Image(
                painter = painterResource(id = R.drawable.firebase_notes_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(NavRoutes.NoteScreen.route) {
            popUpTo(NavRoutes.SplashScreen.route) {
                inclusive = true
            }
        }
    }
}