package com.example.assignment2.ui.audio

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.assignment2.R

/**
 * AudioFragment handles audio playback operations.
 * It provides play, pause, and stop controls.
 */
class AudioFragment : Fragment() {

    // MediaPlayer object to control audio playback
    private var mediaPlayer: MediaPlayer? = null
    // Boolean to track if the MediaPlayer is ready to play
    private var isPrepared = false
    // TextView to display the current status of the audio
    private lateinit var statusView: TextView

    // Inflates the fragment's layout when the view is created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // R.layout.fragment_audio is the XML file for this fragment's UI
        return inflater.inflate(R.layout.fragment_audio, container, false)
    }

    // Called after the view is created; used to initialize UI elements and click listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Finding UI components by their IDs defined in fragment_audio.xml
        statusView = view.findViewById(R.id.tv_audio_status)
        val btnPlay = view.findViewById<Button>(R.id.btn_play)
        val btnPause = view.findViewById<Button>(R.id.btn_pause)
        val btnStop = view.findViewById<Button>(R.id.btn_stop)

        // Initially prepare the player so it's ready when the user taps play
        preparePlayer()

        // Set click listener for the Play button
        btnPlay.setOnClickListener {
            // Re-prepare if the player was released or stopped
            if (mediaPlayer == null) preparePlayer()
            // If prepared, start playback and update status
            if (isPrepared) {
                mediaPlayer?.start()
                statusView.text = "Playing"
            } else {
                // Show a toast if the audio is still loading
                Toast.makeText(requireContext(), "Audio is still loading...", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the Pause button
        btnPause.setOnClickListener {
            mediaPlayer?.let {
                // If audio is playing, pause it and update status
                if (it.isPlaying) {
                    it.pause()
                    statusView.text = "Paused"
                }
            }
        }

        // Set click listener for the Stop button
        btnStop.setOnClickListener {
            mediaPlayer?.let {
                // If it's playing or prepared, stop it
                if (it.isPlaying || isPrepared) {
                    it.stop()
                    isPrepared = false
                    statusView.text = "Stopped"
                    // Re-prepare so it can be played again from the beginning
                    preparePlayer()
                }
            }
        }
    }

    // Initializes the MediaPlayer with a local resource file
    private fun preparePlayer() {
        // Ensure any existing player is released before creating a new one
        releasePlayer()
        
        // Create MediaPlayer using the sample_audio file in res/raw
        val player = MediaPlayer.create(requireContext(), R.raw.sample_audio)
        if (player != null) {
            mediaPlayer = player
            // MediaPlayer.create() calls prepare() internally, so it's ready immediately
            isPrepared = true
            statusView.text = "Ready"

            // Listener for when the audio finishes playing
            player.setOnCompletionListener {
                statusView.text = "Finished"
            }
            
            // Listener to handle any errors during playback
            player.setOnErrorListener { _, _, _ ->
                statusView.text = "Playback error"
                true
            }
        } else {
            // Show error if the file couldn't be loaded
            statusView.text = "Unable to load audio"
        }
    }

    // Releases MediaPlayer resources to prevent memory leaks
    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
    }

    // Clean up resources when the fragment's view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }
}
