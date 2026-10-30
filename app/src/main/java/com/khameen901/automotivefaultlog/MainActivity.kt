package com.khameen901.automotivefaultlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.khameen901.automotivefaultlog.navigation.AppNavHost
import com.khameen901.automotivefaultlog.ui.theme.AutomotiveFaultLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutomotiveFaultLogTheme(darkTheme = isSystemInDarkTheme()) {
                AppNavHost()
            }
        }
    }
}
