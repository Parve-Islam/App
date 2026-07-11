package com.example.assignment2

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.assignment2.ui.audio.AudioFragment
import com.example.assignment2.ui.broadcast.BroadcastReceiverFragment
import com.example.assignment2.ui.imagescale.ImageScaleFragment
import com.example.assignment2.ui.video.VideoFragment
import com.google.android.material.navigation.NavigationView

/**
 * MainActivity serves as the entry point and hosts the navigation drawer.
 * It manages switching between different fragments.
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // DrawerLayout for the side navigation menu
    private lateinit var drawerLayout: DrawerLayout

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the main layout defined in activity_main.xml
        setContentView(R.layout.activity_main)

        // Setup the toolbar as the action bar for the activity
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Set initial title
        title = getString(R.string.menu_broadcast_receiver)

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        // Set this activity as the listener for navigation item selections
        navView.setNavigationItemSelectedListener(this)

        // ActionBarDrawerToggle connects the drawer layout to the toolbar
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        // Add the toggle as a listener and sync its state (hamburger icon)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Load the default fragment if this is the first time creating the activity
        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.nav_broadcast_receiver)
            loadFragment(BroadcastReceiverFragment())
        }
    }

    // Helper method to swap the current fragment in the content_frame
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    // Handles clicks on the navigation drawer items
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Switch fragment based on the item ID clicked
        when (item.itemId) {
            R.id.nav_broadcast_receiver -> {
                title = getString(R.string.menu_broadcast_receiver)
                loadFragment(BroadcastReceiverFragment())
            }
            R.id.nav_image_scale -> {
                title = getString(R.string.menu_image_scale)
                loadFragment(ImageScaleFragment())
            }
            R.id.nav_video -> {
                title = getString(R.string.menu_video)
                loadFragment(VideoFragment())
            }
            R.id.nav_audio -> {
                title = getString(R.string.menu_audio)
                loadFragment(AudioFragment())
            }
        }
        // Close the navigation drawer after an item is selected
        drawerLayout.closeDrawers()
        return true
    }

    // Handles the back button press to close the drawer if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
            drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
