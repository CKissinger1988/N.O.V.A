package com.spartanai.nova.data.model

data class NovaSettings(
    // AI Settings (Deep Manual Control)
    val aiProvider: String = "Gemini",
    val aiModel: String = "gemini-1.5-pro",
    val aiTemperature: Float = 0.7f,
    val aiTopP: Float = 0.95f,
    val aiTopK: Int = 40,
    val aiMaxTokens: Int = 4096,
    val aiAutoExecute: Boolean = false,
    val aiSystemInstruction: String = "You are N.O.V.A., a sovereign offensive intelligence engine.",
    
    // Terminal & UI Settings
    val terminalFontSize: Int = 12,
    val terminalHistoryLimit: Int = 5000,
    val terminalAutoScroll: Boolean = true,
    val uiAnimationSpeedMs: Int = 300,
    val uiPanelWidthLeft: Int = 280,
    val uiPanelWidthRight: Int = 320,
    val uiHolographicGlowIntensity: Float = 0.8f,
    val uiDisplayScale: Float = 1.0f, // Scale factor for 4K support
    val uiHighResRendering: Boolean = true,
    
    // Security & Cryptography
    val encryptionType: String = "AES-256-GCM + ChaCha20",
    val keyRotationDays: Int = 7,
    val stealthModeActive: Boolean = true,
    val autoVpnEngagement: Boolean = true,
    val proxyType: String = "SOCKS5",
    val proxyHost: String = "127.0.0.1",
    val proxyPort: Int = 9050,
    val antiForensicsEnabled: Boolean = true,
    
    // Recon & Scanning
    val netScanTimeoutMs: Int = 2000,
    val netScanThreads: Int = 50,
    val portScanDelayMs: Int = 100,
    val portScanTimeoutMs: Int = 500,
    val fingerprintingIntensity: Int = 3, // 1-5
    
    // Wireless & Signal Intelligence
    val btScanIntervalMs: Long = 5000,
    val btSignalThresholdDbm: Int = -80,
    val wifiMonitorMode: Boolean = false,
    val nfcAutoClone: Boolean = false,
    
    // Remote Access (ADB/VNC)
    val defaultAdbPort: Int = 5555,
    val adbAutoReconnect: Boolean = true,
    val vncResolutionWidth: Int = 1280,
    val vncResolutionHeight: Int = 720,
    
    // Comms & Exfiltration
    val rocketChatUrl: String = "https://chat.spartan.apex",
    val commsEncryptionActive: Boolean = true,
    val exfiltrationChunkSizeKb: Int = 512,
    
    // Voice & Haptics
    val voiceSpeed: Float = 1.0f,
    val voicePitch: Float = 1.0f,
    val hapticFeedbackEnabled: Boolean = true,

    // Hardware & Power
    val lowPowerMode: Boolean = false,
    val cpuGovernorMode: String = "Performance",

    // Ghost-Tier Protocols
    val duressPin: String = "9999",
    val geofenceEnabled: Boolean = false,
    val safeZoneLat: Double = 0.0,
    val safeZoneLon: Double = 0.0,
    val safeZoneRadiusM: Float = 500f,
    val stegoKey: String = "spartan-stego-key",
    val phishingServerPort: Int = 8080,
    val phishingSslEnabled: Boolean = true
)

