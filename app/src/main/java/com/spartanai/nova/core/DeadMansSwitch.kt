package com.spartanai.nova.core

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Dead Man's Switch (DMS) Subsystem
 * Mandate: Trigger Omega Protocol if user fails to check-in within the interval.
 */
class DeadMansSwitch(private val context: Context, private val orchestrator: NovaOrchestrator) {

    private val _remainingTime = MutableStateFlow(0L)
    val remainingTime = _remainingTime.asStateFlow()

    private var dmsJob: Job? = null
    private var isActive = false

    fun arm(intervalMinutes: Int) {
        if (isActive) stop()
        isActive = true
        val intervalMs = intervalMinutes * 60 * 1000L
        _remainingTime.value = intervalMs
        
        orchestrator.addOutput("[DMS]: System ARMED. Check-in required every $intervalMinutes min.")
        
        dmsJob = GlobalScope.launch(Dispatchers.Default) {
            while (isActive && _remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1000
                
                if (_remainingTime.value <= 10000 && _remainingTime.value > 0) {
                    orchestrator.speak("Warning. Dead man's switch zeroing. Check-in required.")
                }
            }
            
            if (isActive && _remainingTime.value <= 0) {
                orchestrator.addOutput("[DMS]: ZERO HOUR REACHED. Initiating OMEGA...")
                SecurityManager().triggerOmegaProtocol(context)
            }
        }
    }

    fun reset() {
        if (!isActive) return
        // Reset to original interval (or a default)
        _remainingTime.value = 5 * 60 * 1000L // Default 5 mins for now
        orchestrator.addOutput("[DMS]: Check-in verified. Timer reset.")
    }

    fun stop() {
        isActive = false
        dmsJob?.cancel()
        _remainingTime.value = 0
        orchestrator.addOutput("[DMS]: System disarmed.")
    }
}