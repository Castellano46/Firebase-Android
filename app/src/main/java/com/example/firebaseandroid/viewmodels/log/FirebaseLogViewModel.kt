package com.example.firebaseandroid.viewmodels.log

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.crashlytics

class FirebaseLogViewModel(): ViewModel(), LogViewModelInterface {

    private val firebaseAnalytics = Firebase.analytics
    private val firebaseCrashlytics = Firebase.crashlytics

    override fun log(screen: String, action: String) {
        firebaseAnalytics.logEvent(action) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screen)
        }
    }

    override fun crash(screen: String, exception: Exception) {
        firebaseCrashlytics.setCustomKey("Screen", screen)
        firebaseCrashlytics.recordException(exception)
    }


}