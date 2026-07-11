package com.example.assignment2.ui.video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.assignment2.R

/**
 * VideoFragment handles video playback within the app.
 * It uses a VideoView to play a local MP4 file.
 */
class VideoFragment : Fragment() {

    // Inflates the fragment's layout (fragment_video.xml)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    // Called after the view is created; initializes the VideoView and its controls
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find UI components by ID
        val videoView = view.findViewById<VideoView>(R.id.video_view)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_video)

        // Create a MediaController to provide Play/Pause and Seek controls
        val mediaController = MediaController(requireContext())
        // Anchor the controller to the bottom of the VideoView
        mediaController.setAnchorView(videoView)
        // Attach the controller to the VideoView
        videoView.setMediaController(mediaController)

        // Construct a URI for the local video file in res/raw/sample_video.mp4
        // Format: android.resource://[package_name]/[resource_id]
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.sample_video}")

        // Set the URI to the VideoView
        videoView.setVideoURI(videoUri)

        // Listener that triggers when the video is buffered and ready to play
        videoView.setOnPreparedListener {
            // Hide the loading progress bar
            progressBar.visibility = View.GONE
            // Start the video automatically
            videoView.start()
        }

        // Listener to handle errors during video loading or playback
        videoView.setOnErrorListener { _, _, _ ->
            // Hide the progress bar on error
            progressBar.visibility = View.GONE
            // Inform the user about the failure
            Toast.makeText(requireContext(), "Unable to play video", Toast.LENGTH_SHORT).show()
            true // Indicates the error was handled
        }
    }

    // Stop playback when the fragment's view is destroyed to release resources
    override fun onDestroyView() {
        super.onDestroyView()
        view?.findViewById<VideoView>(R.id.video_view)?.stopPlayback()
    }
}
