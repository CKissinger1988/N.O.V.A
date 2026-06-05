package com.spartanai.nova.data.model

data class NovaSettings(
    // AI Settings
    val aiProvider: String = "Gemini",
    val aiModel: String = "gemini-1.5-pro",
    val aiTemperature: Float = 0.7f,
    val aiAutoExecute: Boolean = false,
    
    // Terminal Settings
    val terminalFontSize: Int = 12,
    val terminalHistoryLimit: Int = 1000,
    val terminalAutoScroll: Boolean = true,
    
    // Security Settings
    val encryptionType: String = "AES-256-GCM + ChaCha20",
    val keyRotationDays: Int = 7,
    val stealthModeActive: Boolean = true,
    val autoVpnEngagement: Boolean = true,
    
    // Wireless Settings
    val btScanIntervalMs: Long = 5000,
    val nfcAutoClone: Boolean = false,
    
    // RemoteADB Settings
    val defaultAdbPort: Int = 5555,
    val autoReconnect: Boolean = true,
    
    // Comms Settings
    val rocketChatUrl: String = "https://chat.spartan.apex",
    val commsEncryptionActive: Boolean = true,
    
    // Voice Settings
    val voiceSpeed: Float = 1.0f,
    val voicePitch: Float = 1.0f
)
