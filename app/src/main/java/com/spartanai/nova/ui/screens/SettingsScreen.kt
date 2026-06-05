package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.data.model.NovaSettings

@Composable
fun SettingsScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Mission Configuration", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // AI Configuration
        SettingsCategory("AI Command Center") {
            SettingsTextField("AI Provider", settings.aiProvider) { orchestrator.updateSettings(settings.copy(aiProvider = it)) }
            SettingsTextField("AI Model", settings.aiModel) { orchestrator.updateSettings(settings.copy(aiModel = it)) }
            SettingsSlider("Temperature", settings.aiTemperature, 0f..1f) { orchestrator.updateSettings(settings.copy(aiTemperature = it)) }
            SettingsToggle("Auto-Execute AI Payloads", settings.aiAutoExecute) { orchestrator.updateSettings(settings.copy(aiAutoExecute = it)) }
        }

        // Terminal Configuration
        SettingsCategory("Terminal Interface") {
            SettingsSlider("Font Size", settings.terminalFontSize.toFloat(), 8f..24f) { orchestrator.updateSettings(settings.copy(terminalFontSize = it.toInt())) }
            SettingsTextField("History Limit", settings.terminalHistoryLimit.toString()) { orchestrator.updateSettings(settings.copy(terminalHistoryLimit = it.toIntOrNull() ?: 1000)) }
            SettingsToggle("Auto-Scroll to Bottom", settings.terminalAutoScroll) { orchestrator.updateSettings(settings.copy(terminalAutoScroll = it)) }
        }

        // Security Configuration
        SettingsCategory("SecCom Cryptography") {
            SettingsTextField("Encryption Mode", settings.encryptionType) { orchestrator.updateSettings(settings.copy(encryptionType = it)) }
            SettingsTextField("Key Rotation (Days)", settings.keyRotationDays.toString()) { orchestrator.updateSettings(settings.copy(keyRotationDays = it.toIntOrNull() ?: 7)) }
            SettingsTextField("Duress Kill PIN", settings.duressPin) { orchestrator.updateSettings(settings.copy(duressPin = it)) }
            SettingsToggle("Stealth Ghost Mode", settings.stealthModeActive) { orchestrator.updateSettings(settings.copy(stealthModeActive = it)) }
            SettingsToggle("Auto VPN Engagement", settings.autoVpnEngagement) { orchestrator.updateSettings(settings.copy(autoVpnEngagement = it)) }
        }

        // Geospatial Configuration
        SettingsCategory("Geofence Guardian") {
            SettingsToggle("Enable Geofence OMEGA", settings.geofenceEnabled) { orchestrator.updateSettings(settings.copy(geofenceEnabled = it)) }
            SettingsTextField("Safe Zone Lat", settings.safeZoneLat.toString()) { orchestrator.updateSettings(settings.copy(safeZoneLat = it.toDoubleOrNull() ?: 0.0)) }
            SettingsTextField("Safe Zone Lon", settings.safeZoneLon.toString()) { orchestrator.updateSettings(settings.copy(safeZoneLon = it.toDoubleOrNull() ?: 0.0)) }
            SettingsSlider("Radius (m)", settings.safeZoneRadiusM, 100f..5000f) { orchestrator.updateSettings(settings.copy(safeZoneRadiusM = it)) }
        }

        // Wireless Configuration
        SettingsCategory("Wireless Subsystems") {
            SettingsTextField("BT Scan Interval (ms)", settings.btScanIntervalMs.toString()) { orchestrator.updateSettings(settings.copy(btScanIntervalMs = it.toLongOrNull() ?: 5000)) }
            SettingsToggle("Auto-Clone NFC Tags", settings.nfcAutoClone) { orchestrator.updateSettings(settings.copy(nfcAutoClone = it)) }
        }

        // RemoteADB Configuration
        SettingsCategory("RemoteADB Bridge") {
            SettingsTextField("Default Port", settings.defaultAdbPort.toString()) { orchestrator.updateSettings(settings.copy(defaultAdbPort = it.toIntOrNull() ?: 5555)) }
            SettingsToggle("Auto-Reconnect to Targets", settings.autoReconnect) { orchestrator.updateSettings(settings.copy(autoReconnect = it)) }
        }

        // Voice Configuration
        SettingsCategory("Vocal Interface") {
            SettingsSlider("Voice Speed", settings.voiceSpeed, 0.5f..2.0f) { orchestrator.updateSettings(settings.copy(voiceSpeed = it)) }
            SettingsSlider("Voice Pitch", settings.voicePitch, 0.5f..2.0f) { orchestrator.updateSettings(settings.copy(voicePitch = it)) }
        }

        // Optimization Configuration
        SettingsCategory("Hardware & Performance") {
            SettingsToggle("Low Power Mode (Moto XT2617V Opt)", settings.lowPowerMode) { orchestrator.updateSettings(settings.copy(lowPowerMode = it)) }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { orchestrator.executeCommand("sys factory-reset") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("RESTORE MISSION DEFAULTS")
        }
    }
}

@Composable
fun SettingsCategory(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
        content()
    }
}

@Composable
fun SettingsTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true
    )
}

@Composable
fun SettingsToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingsSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text("%.2f".format(value), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = range)
    }
}
