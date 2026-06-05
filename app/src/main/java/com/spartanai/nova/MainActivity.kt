package com.spartanai.nova

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.spartanai.nova.core.BiometricAuthManager
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.NovaApp
import com.spartanai.nova.ui.theme.NOVATheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize the Supreme Orchestrator
        NovaOrchestrator.getInstance().initialize(this)

        val prefs = getSharedPreferences("nova_prefs", android.content.Context.MODE_PRIVATE)
        val isDisguised = prefs.getBoolean("disguise_mode_active", false)

        if (isDisguised) {
            setupDisguiseContent()
        } else {
            val biometricManager = BiometricAuthManager(this)
            if (biometricManager.isBiometricAvailable()) {
                biometricManager.authenticate(
                    onSuccess = { setupContent() },
                    onError = { finish() }
                )
            } else {
                setupContent()
            }
        }
    }

    private fun setupContent() {
        setContent {
            NOVATheme {
                NovaApp()
            }
        }
    }

    private fun setupDisguiseContent() {
        setContent {
            androidx.compose.material3.MaterialTheme {
                BakingScreen()
            }
        }
    }
}
