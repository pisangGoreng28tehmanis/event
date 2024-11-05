package sinaga.modern.event.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sinaga.modern.event.data.FavoriteEventRepository
import sinaga.modern.event.di.Injection

class ViewModelFactory private constructor(
    private val favoriteEventRepository: FavoriteEventRepository,
    private val settingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteEventViewModel::class.java) -> {
                FavoriteEventViewModel(favoriteEventRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(settingPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context), // FavoriteEventRepository
                    Injection.provideSettingPreferences(context) // SettingPreferences
                )
            }.also { instance = it }
    }
}