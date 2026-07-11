package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.ui.broadcast.BroadcastReceiverFragment.Companion.EXTRA_TYPE
import com.example.assignment2.ui.broadcast.BroadcastReceiverFragment.Companion.TYPE_CUSTOM

/**
 * Second Activity - behaviour depends on the option chosen in the Broadcast Receiver fragment.
 * 
 * It handles two modes:
 * 1. Custom: Allows the user to enter text and pass it to the next activity.
 * 2. Battery: Displays the current battery percentage using a system broadcast receiver.
 */
class SecondActivity : AppCompatActivity() {

    // Variable to store which type was selected (Custom or Battery)
    private var selectedType: String = TYPE_CUSTOM

    // BroadcastReceiver implementation for monitoring system battery changes
    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Extract battery level and scale from the system intent
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            
            // Calculate and display the percentage if data is valid
            if (level >= 0 && scale > 0) {
                val percentage = (level * 100) / scale
                findViewById<TextView>(R.id.tv_battery_percentage).text = "$percentage%"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for the second activity
        setContentView(R.layout.activity_second)

        // Retrieve the selection type passed from the previous screen
        selectedType = intent.getStringExtra(EXTRA_TYPE) ?: TYPE_CUSTOM

        // Initialize the UI based on the selected mode
        if (selectedType == TYPE_CUSTOM) {
            setupCustomUi()
        } else {
            setupBatteryUi()
        }
    }

    /**
     * Initializes the UI components for the "Custom Broadcast" mode.
     */
    private fun setupCustomUi() {
        // Show the layout containing the EditText and Send button
        findViewById<android.widget.LinearLayout>(R.id.layout_custom).visibility =
            android.view.View.VISIBLE

        val etMessage = findViewById<EditText>(R.id.et_message)
        val btnSend = findViewById<Button>(R.id.btn_send)

        // Handle the send button click
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            // Validate that the message is not empty
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Navigate to ThirdActivity and pass the message
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra(EXTRA_TYPE, selectedType)
            intent.putExtra(ThirdActivity.EXTRA_MESSAGE, message)
            startActivity(intent)
        }
    }

    /**
     * Initializes the UI components for the "Battery Notification" mode.
     */
    private fun setupBatteryUi() {
        // Show the layout containing the battery percentage display
        findViewById<android.widget.LinearLayout>(R.id.layout_battery).visibility =
            android.view.View.VISIBLE

        val btnNext = findViewById<Button>(R.id.btn_next)
        // Handle the "Next" button click to move to the next screen
        btnNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra(EXTRA_TYPE, selectedType)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // If in battery mode, register the receiver to start listening for changes
        if (selectedType != TYPE_CUSTOM) {
            registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the receiver when the activity is not visible to save battery
        if (selectedType != TYPE_CUSTOM) {
            unregisterReceiver(batteryReceiver)
        }
    }
}
