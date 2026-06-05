package com.spartanai.nova.core

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.spartanai.nova.data.model.DiscoveredDevice
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.util.Formatter

/**
 * N.O.V.A. Network Scanner (ARP Recon Subsystem)
 * Mandate: Discover active nodes on the local subnet via ARP table analysis and active probing.
 */
class NetworkScanner(private val orchestrator: NovaOrchestrator) {

    private val tag = "NetworkScanner"

    fun scanLocalSubnet(): List<DiscoveredDevice> {
        val devices = mutableListOf<DiscoveredDevice>()
        
        // Phase 1: Active Probing to populate ARP table
        orchestrator.logAndSpeak("[NETWORK]: Initiating parallel coroutine ping sweep...", speak = true)
        performActiveProbing()
        
        try {
            orchestrator.addOutput("[NETWORK]: Parsing kernel ARP table (ip neigh)...")
            val process = Runtime.getRuntime().exec("ip neigh")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val device = parseNeighborLine(line!!)
                if (device != null) {
                    // Phase 2: Hostname Resolution (Reverse DNS)
                    val hostname = try {
                        InetAddress.getByName(device.ip).canonicalHostName
                    } catch (e: Exception) {
                        "Unknown"
                    }
                    
                    val enrichedDevice = device.copy(
                        hostname = if (hostname == device.ip) "No PTR" else hostname
                    )
                    
                    devices.add(enrichedDevice)
                    orchestrator.logAndSpeak("[SCAN]: Discovered ${enrichedDevice.ip} [${enrichedDevice.mac}] -> ${enrichedDevice.hostname}")
                }
            }
            process.waitFor()
            
            if (devices.isNotEmpty()) {
                orchestrator.logAndSpeak("[RECON]: Subnet scan finalized. ${devices.size} active nodes identified.", speak = true)
            } else {
                orchestrator.logAndSpeak("[RECON]: Subnet scan complete. No external nodes detected.", speak = true)
            }
        } catch (e: Exception) {
            Log.e(tag, "ARP scan failed: ${e.message}")
            orchestrator.logAndSpeak("[ERROR]: Network recon failure: ${e.message}", speak = true)
        }
        return devices
    }


    private fun performActiveProbing() {
        val baseIP = getLocalIpPrefix() ?: return
        orchestrator.addOutput("[NETWORK]: Targeting range $baseIP.0/24...")
        
        // Multi-threaded ping sweep (parallel coroutines)
        runBlocking {
            (1..254).map { i ->
                async(Dispatchers.IO) {
                    try {
                        val host = "$baseIP.$i"
                        val address = InetAddress.getByName(host)
                        if (address.isReachable(500)) {
                            // Kernel ARP table is updated automatically when we reach a host
                        }
                    } catch (e: Exception) {
                        // Ignore
                    }
                }
            }.awaitAll()
        }
    }

    private fun getLocalIpPrefix(): String? {
        try {
            val process = Runtime.getRuntime().exec("ip addr show wlan0")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.contains("inet ")) {
                    val parts = line!!.trim().split("\\s+".toRegex())
                    val ip = parts[1].substringBefore("/")
                    return ip.substringBeforeLast(".")
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to get local IP: ${e.message}")
        }
        return null
    }

    private fun parseNeighborLine(line: String): DiscoveredDevice? {
        // Typical format: 192.168.1.1 dev wlan0 lladdr ac:84:c9:86:14:e4 REACHABLE
        val parts = line.split("\\s+".toRegex())
        if (parts.size < 4) return null
        
        val ip = parts[0]
        val macIndex = parts.indexOf("lladdr")
        if (macIndex != -1 && macIndex + 1 < parts.size) {
            val mac = parts[macIndex + 1]
            // Filter out incomplete entries
            if (mac == "FAILED" || mac == "INCOMPLETE") return null
            
            return DiscoveredDevice(
                ip = ip,
                mac = mac
            )
        }
        return null
    }
}