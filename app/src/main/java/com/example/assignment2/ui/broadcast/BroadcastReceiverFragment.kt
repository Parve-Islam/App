package com.example.assignment2.ui.broadcast

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.assignment2.R
import com.example.assignment2.SecondActivity

/**
 * BroadcastReceiverFragment is the initial screen for the "Broadcast Receiver" feature.
 * It allows users to choose between a Custom broadcast or a System battery notification.
 */
class BroadcastReceiverFragment : Fragment() {

    // List of options displayed in the dropdown (Spinner)
    private val options = arrayOf(
        "Custom broadcast receiver",
        "System battery notification receiver"
    )

    // Inflates the fragment layout (fragment_broadcast_receiver.xml)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_broadcast_receiver, container, false)
    }

    // Called after the UI is ready
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the Spinner and Button in the layout
        val spinner = view.findViewById<Spinner>(R.id.spinner_broadcast_type)
        val btnProceed = view.findViewById<Button>(R.id.btn_proceed)

        // Create an adapter to populate the Spinner with our options array
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Handle the "Proceed" button click
        btnProceed.setOnClickListener {
            // Determine which type was selected based on position (0 = Custom, 1 = Battery)
            val selectedType = if (spinner.selectedItemPosition == 0) TYPE_CUSTOM else TYPE_BATTERY
            
            // Create an Intent to launch SecondActivity
            val intent = Intent(requireContext(), SecondActivity::class.java)
            // Pass the selected type as an extra so the next activity knows what to do
            intent.putExtra(EXTRA_TYPE, selectedType)
            startActivity(intent)
        }
    }

    // Companion object for constants used across activities
    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_CUSTOM = "custom"
        const val TYPE_BATTERY = "battery"
    }
}
