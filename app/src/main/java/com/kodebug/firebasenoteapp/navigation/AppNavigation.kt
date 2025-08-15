package com.kodebug.firebasenoteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kodebug.firebasenoteapp.presentation.sceens.AddNoteScreen
import com.kodebug.firebasenoteapp.presentation.sceens.NoteScreen
import com.kodebug.firebasenoteapp.presentation.sceens.SplashScreen
import com.kodebug.firebasenoteapp.presentation.sceens.UpdateNoteScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SplashScreen.route
    ){
        composable(NavRoutes.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.NoteScreen.route) {
            NoteScreen(navController = navController)
        }

        composable(NavRoutes.AddNoteScreen.route) {
            AddNoteScreen(navController = navController)
        }

        composable(NavRoutes.UpdateNoteScreen.route+"/{noteId}") {
            val noteId = it.arguments?.getString("noteId")
            UpdateNoteScreen(navController = navController, noteId = noteId)
        }
    }
}