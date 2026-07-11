package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.assignment2.ui.broadcast.BroadcastReceiverFragment.Companion.EXTRA_TYPE
import com.example.assignment2.ui.broadcast.BroadcastReceiverFragment.Companion.TYPE_CUSTOM

/**
 * Third Activity - the final screen in the broadcast receiver flow.
 *
 * 1. If "Custom broadcast" was chosen: This activity registers a receiver for a 
 *    custom action, then triggers a broadcast to itself to display the received message.
 * 2. If "Battery notification" was chosen: It simply displays a placeholder message.
 */
class ThirdActivity : AppCompatActivity() {

    // Tracks whether we are in "Custom" mode
    private var isCustom = false

    // Custom BroadcastReceiver that listens for our specific internal action
    private val customReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Retrieve the message string from the broadcast intent
            val received = intent.getStringExtra(EXTRA_MESSAGE) ?: ""
            // Update the UI with the message received via the broadcast
            findViewById<TextView>(R.id.tv_result_value).text = received
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the third activity layout
        setContentView(R.layout.activity_third)

        // Determine the mode based on the intent extra
        val type = intent.getStringExtra(EXTRA_TYPE) ?: TYPE_CUSTOM
        isCustom = type == TYPE_CUSTOM

        val labelView = findViewById<TextView>(R.id.tv_result_label)
        val valueView = findViewById<TextView>(R.id.tv_result_value)

        if (isCustom) {
            // Setup UI for custom message display
            labelView.text = getString(R.string.received_message_label)
            valueView.text = ""

            // Register the receiver for the custom action locally
            val filter = IntentFilter(CUSTOM_ACTION)
            ContextCompat.registerReceiver(
                this, customReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED
            )

            // Trigger the broadcast. We send the message that was originally 
            // passed to this activity via its starting intent.
            val message = intent.getStringExtra(EXTRA_MESSAGE) ?: ""
            val broadcastIntent = Intent(CUSTOM_ACTION)
            broadcastIntent.putExtra(EXTRA_MESSAGE, message)
            // Ensure the broadcast is only delivered within our app
            broadcastIntent.setPackage(packageName)
            sendBroadcast(broadcastIntent)
        } else {
            // UI for when no action is needed (Battery mode)
            labelView.text = getString(R.string.no_action_battery)
            valueView.text = ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver to prevent memory leaks if it was registered
        if (isCustom) {
            unregisterReceiver(customReceiver)
        }
    }

    companion object {
        // Keys for intent extras and custom broadcast action
        const val EXTRA_MESSAGE = "extra_message"
        const val CUSTOM_ACTION = "com.example.assignment2.CUSTOM_BROADCAST_ACTION"
    }
}
