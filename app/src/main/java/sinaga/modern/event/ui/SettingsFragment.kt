package sinaga.modern.event.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import sinaga.modern.event.R
import sinaga.modern.event.di.Injection

class SettingsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get an instance of ViewModelFactory using getInstance()
        val factory = ViewModelFactory.getInstance(requireContext())

        // Initialize the ViewModel using the factory
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Inisialisasi SwitchMaterial untuk tema
        val switchTheme = view.findViewById<SwitchMaterial>(R.id.switch_theme)

        // Observe the theme settings from ViewModel
        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            switchTheme.isChecked = isDarkModeActive
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Listener untuk perubahan status Switch
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        return view
    }
}