package com.spartanai.nova.core

import android.util.Log
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.net.Socket

/**
 * N.O.V.A. Port Scanner Subsystem
 * Mandate: Identify open services and potential entry points on target nodes.
 */
class PortScanner(private val orchestrator: NovaOrchestrator) {

    private val commonPorts = mapOf(
        21 to "FTP",
        22 to "SSH",
        23 to "Telnet",
        53 to "DNS",
        80 to "HTTP",
        443 to "HTTPS",
        445 to "SMB",
        1433 to "MSSQL",
        3306 to "MySQL",
        3389 to "RDP",
        5432 to "PostgreSQL",
        5555 to "ADB",
        8080 to "HTTP-Alt"
    )

    suspend fun scanPorts(ip: String): List<Int> = withContext(Dispatchers.IO) {
        val openPorts = mutableListOf<Int>()
        orchestrator.addOutput("[PORT-SCAN]: Probing high-value services on $ip...")
        
        val jobs = commonPorts.keys.map { port ->
            async {
                if (isPortOpen(ip, port)) {
                    synchronized(openPorts) { openPorts.add(port) }
                    val service = commonPorts[port] ?: "Unknown"
                    orchestrator.addOutput("[PORT]: Discovered OPEN port $port ($service) on $ip")
                }
            }
        }
        jobs.awaitAll()
        
        openPorts.sorted()
    }

    private fun isPortOpen(ip: String, port: Int): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 600)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun guessOS(ip: String, openPorts: List<Int>): String {
        return when {
            openPorts.contains(5555) -> "Android (Rooted/ADB)"
            openPorts.contains(445) && openPorts.contains(3389) -> "Windows Server / Desktop"
            openPorts.contains(445) -> "Windows (SMB)"
            openPorts.contains(22) && (openPorts.contains(80) || openPorts.contains(443)) -> "Linux Web Server"
            openPorts.contains(22) -> "Linux / Unix (SSH)"
            openPorts.contains(1433) || openPorts.contains(3306) || openPorts.contains(5432) -> "Database Host"
            openPorts.contains(62078) -> "iOS/Apple Mobile"
            else -> "Hardened / Unknown OS"
        }
    }
}