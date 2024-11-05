package sinaga.modern.event.di

import android.content.Context
import sinaga.modern.event.data.FavoriteEventRepository
import sinaga.modern.event.data.local.FavoriteEventRoomDatabase
import sinaga.modern.event.data.retrofit.ApiConfig
import sinaga.modern.event.ui.SettingPreferences
import sinaga.modern.event.ui.dataStore
import sinaga.modern.event.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteEventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventRoomDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()

        // Create an instance of AppExecutors
        val appExecutors = AppExecutors()

        // Pass appExecutors when calling getInstance
        return FavoriteEventRepository.getInstance(apiService, dao, appExecutors)
    }

    fun provideSettingPreferences(context: Context): SettingPreferences {
        // Use the public method to get an instance of SettingPreferences
        return SettingPreferences.getInstance(context.dataStore)
    }
}
