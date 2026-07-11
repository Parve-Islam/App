package com.example.assignment2.ui.imagescale

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.assignment2.R

/**
 * ImageScaleFragment loads a high-quality image from the internet and allows
 * the user to perform pinch-to-zoom scaling.
 */
class ImageScaleFragment : Fragment() {

    // High-resolution landscape image URL
    private val imageUrl =
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1200"

    private lateinit var imageView: ImageView
    // Matrix object used to track and apply transformations (scaling) to the image
    private val matrix = Matrix()
    // Current zoom level
    private var scaleFactor = 1f
    // Boundary limits for zooming
    private val minScale = 0.5f
    private val maxScale = 6f

    // Detector for multi-touch scale (pinch) gestures
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the image scaling UI
        return inflater.inflate(R.layout.fragment_image_scale, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.iv_scalable_image)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_image)

        // Use Glide library to load the remote image efficiently
        Glide.with(this)
            .load(imageUrl)
            .listener(object : RequestListener<android.graphics.drawable.Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<android.graphics.drawable.Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide loading spinner if image fails to load
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: android.graphics.drawable.Drawable,
                    model: Any,
                    target: Target<android.graphics.drawable.Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide loading spinner once image is successfully loaded
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView)

        // Initialize the gesture detector with our custom listener
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        // Attach a touch listener to the ImageView to pass touch events to the detector
        imageView.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)
            // Ensure parent containers (like scroll views) don't steal the touch events
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            true
        }
    }

    /**
     * Inner class to handle pinch-to-zoom logic.
     */
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Calculate new scale factor based on gesture
            var newScaleFactor = scaleFactor * detector.scaleFactor
            // Constrain the zoom between min and max limits
            newScaleFactor = newScaleFactor.coerceIn(minScale, maxScale)
            // Calculate relative scale for matrix math
            val appliedFactor = newScaleFactor / scaleFactor
            scaleFactor = newScaleFactor

            // Apply scaling to the matrix at the focal point of the pinch
            matrix.postScale(
                appliedFactor, appliedFactor,
                detector.focusX, detector.focusY
            )
            // Apply the transformation matrix to the ImageView
            imageView.imageMatrix = matrix
            return true
        }
    }
}
