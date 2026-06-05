package com.spartanai.nova.core

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * Panic Gesture System
 * Mandate: Physical motion-based kill switch for emergency data destruction.
 */
class PanicManager(private val context: Context, private val orchestrator: NovaOrchestrator) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var acceleration = 10f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH

    private val SHAKE_THRESHOLD = 15f // Adjust sensitivity for field ops

    fun startListening() {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        orchestrator.addOutput("[PANIC]: Motion kill-switch ARMING...")
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        
        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt(x * x + y * y + z * z)
        val delta = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta
        
        if (acceleration > SHAKE_THRESHOLD) {
            orchestrator.addOutput("[PANIC]: CRITICAL MOTION DETECTED.")
            SecurityManager().triggerOmegaProtocol(context)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}