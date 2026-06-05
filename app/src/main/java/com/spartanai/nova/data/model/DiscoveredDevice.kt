package com.spartanai.nova.data.model

data class DiscoveredDevice(
    val ip: String,
    val mac: String,
    val hostname: String = "Unknown",
    val vendor: String = "Unknown",
    val openPorts: List<Int> = emptyList(),
    val guessedOS: String = "Unknown",
    val status: DeviceStatus = DeviceStatus.DISCOVERED,
    val lastSeen: Long = System.currentTimeMillis()
)

enum class DeviceStatus {
    DISCOVERED, SCANNING, VULNERABLE, BREACHED, SECURE
}