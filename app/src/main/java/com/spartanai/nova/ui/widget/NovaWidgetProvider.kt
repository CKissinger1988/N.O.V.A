package com.spartanai.nova.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.spartanai.nova.MainActivity
import com.spartanai.nova.R
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.core.SecurityManager

class NovaWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // Initialize orchestrator if needed
        NovaOrchestrator.getInstance().initialize(context)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Initialize orchestrator if needed
        NovaOrchestrator.getInstance().initialize(context)
        super.onReceive(context, intent)
        
        val prefs = context.getSharedPreferences("nova_prefs", Context.MODE_PRIVATE)
        when (intent.action) {
            "ACTION_NOVA_WIDGET_CLICK" -> {
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(launchIntent)
            }
            "ACTION_NOVA_WIDGET_AI" -> {
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("TARGET_SCREEN", "AI")
                }
                context.startActivity(launchIntent)
            }
            "ACTION_NOVA_WIDGET_SCAN" -> {

                NovaOrchestrator.getInstance().executeCommand("war-room scan")
                refreshAllWidgets(context)
            }
            "ACTION_NOVA_WIDGET_OMEGA" -> {
                SecurityManager().triggerOmegaProtocol(context)
                refreshAllWidgets(context)
            }
            "ACTION_NOVA_WIDGET_RECIPE" -> {
                val currentRecipe = prefs.getInt("baking_recipe_index", 0)
                val nextRecipe = (currentRecipe + 1) % 3
                prefs.edit().putInt("baking_recipe_index", nextRecipe).apply()
                refreshAllWidgets(context)
            }
            "ACTION_NOVA_WIDGET_TIMER" -> {
                val timerActive = prefs.getBoolean("baking_timer_active", false)
                prefs.edit().putBoolean("baking_timer_active", !timerActive).apply()
                refreshAllWidgets(context)
            }
        }
    }

    companion object {
        private val RECIPES = arrayOf("Sourdough Bread", "Lemon Cupcake", "Chocolate Cookies")
        private val OVEN_TEMPS = arrayOf("220°C (Preheated)", "180°C (Preheating)", "175°C (Ready)")

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val orchestrator = NovaOrchestrator.getInstance()
            val status = orchestrator.systemStatus.value

            val prefs = context.getSharedPreferences("nova_prefs", Context.MODE_PRIVATE)
            val isDisguised = prefs.getBoolean("disguise_mode_active", false)

            val views = RemoteViews(context.packageName, R.layout.nova_widget)

            if (isDisguised) {
                // Display Baking Disguise View
                views.setViewVisibility(R.id.widget_nova_container, View.GONE)
                views.setViewVisibility(R.id.widget_baking_container, View.VISIBLE)

                // Read baking stats
                val recipeIdx = prefs.getInt("baking_recipe_index", 0)
                val timerActive = prefs.getBoolean("baking_timer_active", false)

                views.setTextViewText(R.id.widget_recipe_name, "Recipe: ${RECIPES[recipeIdx]}")
                views.setTextViewText(R.id.widget_oven_temp, "Oven: ${OVEN_TEMPS[recipeIdx]}")
                
                // Update AI Chef Feed (Disguised Tactical Advisory)
                val advisory = orchestrator.tacticalAdvisory.value
                views.setTextViewText(R.id.widget_chef_advisory, "Optimal consistency: $advisory")

                if (timerActive) {
                    views.setTextViewText(R.id.widget_timer_status, "Timer: 15m remaining")
                    views.setTextViewText(R.id.widget_btn_timer, "STOP")
                } else {
                    views.setTextViewText(R.id.widget_timer_status, "Timer: Ready")
                    views.setTextViewText(R.id.widget_btn_timer, "TIMER")
                }

                // Header / Background click to launch MainActivity (opens BakingScreen)
                val clickIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_CLICK"
                }
                val pendingClick = PendingIntent.getBroadcast(
                    context, 100, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_baking_logo, pendingClick)
                views.setOnClickPendingIntent(R.id.widget_baking_title, pendingClick)

                // Next Recipe button
                val recipeIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_RECIPE"
                }
                val pendingRecipe = PendingIntent.getBroadcast(
                    context, 101, recipeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_recipe, pendingRecipe)

                // AI Chef button (Disguised AI Hub)
                val aiIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_AI"
                }
                val pendingAi = PendingIntent.getBroadcast(
                    context, 103, aiIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_chef, pendingAi)

                // Start Timer button
                val timerIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_TIMER"
                }
                val pendingTimer = PendingIntent.getBroadcast(
                    context, 102, timerIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_timer, pendingTimer)

            } else {
                // Display N.O.V.A. SOC View
                views.setViewVisibility(R.id.widget_nova_container, View.VISIBLE)
                views.setViewVisibility(R.id.widget_baking_container, View.GONE)

                // Update system telemetry
                views.setTextViewText(R.id.widget_cpu, "CPU: ${status.cpuUsage}%")
                views.setTextViewText(R.id.widget_ram, "RAM: ${status.ramUsage}%")
                views.setTextViewText(R.id.widget_battery, "BATT: ${status.batteryLevel}%")
                views.setTextViewText(R.id.widget_node_count, "NODES: ${status.nodeCount}")
                views.setTextViewText(R.id.widget_c2, "C2: ${status.c2LinkStatus.uppercase()}")
                views.setTextViewText(R.id.widget_threat, "THREAT: ${status.threatLevel}")

                // Update Gemini Feed
                val advisory = orchestrator.tacticalAdvisory.value
                views.setTextViewText(R.id.widget_ai_advisory, advisory)

                // Header / Background click to launch MainActivity (opens standard security app)
                val clickIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_CLICK"
                }
                val pendingClick = PendingIntent.getBroadcast(
                    context, 200, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_nova_logo, pendingClick)
                views.setOnClickPendingIntent(R.id.widget_nova_title, pendingClick)

                // Gemini button
                val aiIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_AI"
                }
                val pendingAi = PendingIntent.getBroadcast(
                    context, 203, aiIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_ai, pendingAi)

                // Quick Scan button

                val scanIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_SCAN"
                }
                val pendingScan = PendingIntent.getBroadcast(
                    context, 201, scanIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_scan, pendingScan)

                // Omega Wiper button
                val omegaIntent = Intent(context, NovaWidgetProvider::class.java).apply {
                    action = "ACTION_NOVA_WIDGET_OMEGA"
                }
                val pendingOmega = PendingIntent.getBroadcast(
                    context, 202, omegaIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_omega, pendingOmega)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun refreshAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, NovaWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
