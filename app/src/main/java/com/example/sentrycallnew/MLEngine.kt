package com.example.sentrycallnew

import android.util.Log

class MLEngine {

    fun isImportantCall(isKnown: Boolean): Boolean {

        if (isKnown) {
            Log.d("ML_ENGINE", "Important call (favorite contact)")
            return true
        } else {
            Log.d("ML_ENGINE", "Not important (unknown contact)")
            return false
        }
    }
}