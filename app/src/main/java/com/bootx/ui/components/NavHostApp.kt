package com.bootx.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bootx.ui.navigation.Destinations
import com.bootx.ui.screens.ListScreen
import com.bootx.ui.screens.MainScreen
import com.bootx.ui.screens.PlayScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavHostApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.MainFrame.route,
    ) {
        composable(
            Destinations.MainFrame.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
        ) {
            MainScreen(navController)
        }
        composable(
            Destinations.PlayFrame.route + "/{fsId}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
        ) {
            val fsId = it.arguments?.getString("fsId") ?: ""
            PlayScreen(navController, fsId)
        }
        composable(
            Destinations.ListFrame.route + "/{fsId}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
        ) {
            val fsId = it.arguments?.getString("fsId") ?: ""
            ListScreen(navController, fsId)
        }
    }
}