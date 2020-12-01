package com.notification.compat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class IntentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)
    }
}