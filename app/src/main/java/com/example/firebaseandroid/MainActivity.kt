package com.example.firebaseandroid

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseandroid.ui.screens.AuthScreen
import com.example.firebaseandroid.ui.screens.WallScreen
import com.example.firebaseandroid.viewmodels.auth.DummyAuthViewModel
import com.example.firebaseandroid.viewmodels.auth.FirebaseAuthViewModel
import com.example.firebaseandroid.viewmodels.log.DummyLogViewModel
import com.example.firebaseandroid.viewmodels.log.FirebaseLogViewModel
import com.example.firebaseandroid.viewmodels.storage.DummyStorageViewModel
import com.example.firebaseandroid.viewmodels.storage.FirebaseStorageViewModel
import com.example.firebaseandroid.viewmodels.tweets.DummyTweetsViewModel
import com.example.firebaseandroid.viewmodels.tweets.FirestoreTweetsViewModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.messaging

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Firebase.messaging.subscribeToTopic("KEEPCODING_16")
                .addOnCompleteListener { task ->
                    var msg = "Subscribed OK!"
                    if(!task.isSuccessful) {
                        msg = "Subscribed KO!"
                    }
                    Log.d("FCM", msg)
                }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        askNotificationPermission()

        setContent {
            val navController = rememberNavController()
            val authViewModel = FirebaseAuthViewModel()
            val tweetsViewModel = FirestoreTweetsViewModel()
            val storageViewModel = FirebaseStorageViewModel()
            val logViewModel = FirebaseLogViewModel()

            NavHost(navController = navController, startDestination = Routes.AUTH_SCREEN){
                composable(Routes.AUTH_SCREEN) {
                    AuthScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        logViewModel = logViewModel
                    )
                }
                composable(Routes.WALL_SCREEN) {
                    WallScreen(
                        tweetsViewModel = tweetsViewModel,
                        authViewModel = authViewModel,
                        navController = navController,
                        storageViewModel = storageViewModel,
                        logViewModel = logViewModel
                    )
                }
            }
        }
    }
}