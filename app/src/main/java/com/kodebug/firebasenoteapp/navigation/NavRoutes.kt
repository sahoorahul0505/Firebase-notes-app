package com.kodebug.firebasenoteapp.navigation

sealed class NavRoutes(val route : String) {
    object SplashScreen : NavRoutes(route = "splash")
    object NoteScreen : NavRoutes(route = "home")
    object AddNoteScreen : NavRoutes(route = "add_note")
    object UpdateNoteScreen : NavRoutes(route = "update_note")
}