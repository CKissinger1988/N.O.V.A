package com.spartanai.nova

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.NovaApp
import com.spartanai.nova.ui.theme.NOVATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize the Supreme Orchestrator
        NovaOrchestrator.getInstance().initialize(this)

        setContent {
            NOVATheme {
                NovaApp()
            }
        }
    }
}