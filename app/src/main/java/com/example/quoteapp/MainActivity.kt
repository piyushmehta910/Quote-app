package com.example.quoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.navigation.AppNavigation
import com.example.quoteapp.ui.theme.QuoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ProjectRepository.init(applicationContext)
        setContent {
            QuoteAppTheme {
                AppNavigation()
            }
        }
    }
}
