package sinaga.modern.event.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import sinaga.modern.event.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
