package com.spartanai.nova.core

import android.content.Context
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Duress Security System
 * Mandate: Silent purge and decoy activation upon compromised authentication.
 */
class DuressManager(private val context: Context, private val orchestrator: NovaOrchestrator) {

    fun checkDuress(inputPin: String): Boolean {
        val settings = orchestrator.settings.value
        if (inputPin == settings.duressPin) {
            triggerSilentPurge()
            return true
        }
        return false
    }

    private fun triggerSilentPurge() {
        orchestrator.addOutput("[DURESS]: Silent kill triggered. Dispatching encrypted SOS beacon...")
        
        // 1. Dispatch SOS Beacon via UDP broadcast
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val socket = java.net.DatagramSocket()
                socket.broadcast = true
                val message = "SOS: COMPROMISED".toByteArray()
                val packet = java.net.DatagramPacket(message, message.size, java.net.InetAddress.getByName("255.255.255.255"), 1337)
                socket.send(packet)
                socket.close()
            } catch (e: Exception) {
                // Ignore silent failure
            }
        }
        
        // 2. Wipe sensitive data and swap to Master Baker
        val securityManager = SecurityManager()
        securityManager.triggerOmegaProtocol(context)
    }
}
