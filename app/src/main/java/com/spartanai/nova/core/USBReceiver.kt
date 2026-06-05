package com.spartanai.nova.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log

class USBReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
            Log.d("USBReceiver", "Physical device attached.")
            NovaOrchestrator.getInstance().executeCommand("usb-exploit")
        }
    }
}