package com.alanford.carpediem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.networking.Networking
import com.alanford.carpediem.notifications.AUTHOR
import com.alanford.carpediem.notifications.QUOTE_ID
import com.alanford.carpediem.notifications.QUOTE_TEXT
import com.alanford.carpediem.quotedetails.QuoteDetailFragmentArgs
import com.alanford.carpediem.quotelist.QuoteListFragmentDirections
import com.alanford.carpediem.repository.FavoriteQuotesRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    object Constants {
        const val CARPE_DIEM_API_URL: String = "https://sieze-the-day.herokuapp.com/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // navigation
        setupNavigationAndMenu()

        // networking
        Networking.initialize()

        // database
        FavoriteQuotesRepository.initialize(this)

        // notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                getString(R.string.quote_notification_channel_id),
                getString(R.string.notification_channel_name)
            )
        }
        handleNewIntent(intent)
    }

    private fun handleNewIntent(intent: Intent?) {
        intent?.extras?.let {
            val theQuoteText = it.getString(QUOTE_TEXT)
            val theAuthor = it.getString(AUTHOR)
            val theId = it.getString(QUOTE_ID)

            if (theAuthor != null && theQuoteText != null && theId != null) {
                val action = QuoteListFragmentDirections.actionQuoteListFragmentToQuoteFragment(
                    Quote(
                        id = theId,
                        quoteText = theQuoteText,
                        author = theAuthor
                    )
                )
                val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
                val navController = host.navController
                navController.navigate(action)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // called when we get a notification and the app is open but in the background
        handleNewIntent(intent)
    }

    private fun setupNavigationAndMenu() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        val drawerLayout: DrawerLayout? = findViewById(R.id.main_drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest, R.id.favoritesFragment),
            drawerLayout
        )

        setupActionBar(navController, appBarConfiguration)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView == null) {
            menuInflater.inflate(R.menu.nav_drawer_menu, menu)
            return true
        }

        return retValue
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}
