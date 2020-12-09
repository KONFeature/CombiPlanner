package com.nivelais.combiplanner.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.nivelais.combiplanner.app.ui.modules.Navigator
import com.nivelais.combiplanner.app.ui.theme.CombiPlannerTheme

/**
 * Entry point of the application
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content of our app (Our custom MaterialTheme and then the app navigator)
        setContent {
            CombiPlannerTheme {
                Navigator()
            }
        }
    }
}