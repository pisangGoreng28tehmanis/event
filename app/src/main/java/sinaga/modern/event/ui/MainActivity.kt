package sinaga.modern.event.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import sinaga.modern.event.R
import sinaga.modern.event.di.Injection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get an instance of ViewModelFactory using the getInstance() method
        val factory = ViewModelFactory.getInstance(this)

        // Correctly get instances of the required dependencies
        val pref = SettingPreferences.getInstance(application.dataStore)
        val favoriteEventRepository = Injection.provideRepository(this) // Get the repository instance

        // Use the factory to get the ViewModel
        val mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        MobileAds.initialize(this) {}

        // Jika fragment belum diinisialisasi, tampilkan fragment default
        if (savedInstanceState == null) {
            loadFragment(DefaultFragment())
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_events -> UpcomingFragment()
                R.id.nav_done -> FinishedFragment()
                R.id.nav_home -> DefaultFragment()
                R.id.nav_fav -> FavoriteFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Periksa apakah fragment saat ini sudah dimuat untuk menghindari reload
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (currentFragment?.javaClass == fragment.javaClass) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}
