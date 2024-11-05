package sinaga.modern.event.data

import ApiService
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import sinaga.modern.event.data.local.FavoriteEventDao
import sinaga.modern.event.data.local.FavoriteEvent
import sinaga.modern.event.utils.AppExecutors

class FavoriteEventRepository private constructor(
    private val apiService: ApiService,
    private val favDao: FavoriteEventDao,
    private val appExecutors: AppExecutors // Keep this line to use AppExecutors
) {

    fun getAllFavoriteEvents(): LiveData<Result<List<FavoriteEvent>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val favoriteEvents = favDao.getAllFavoriteEvents()
            emitSource(favoriteEvents.map { Result.Success(it) })
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun addFavorite(event: FavoriteEvent) {
        favDao.insert(listOf(event))
    }

    suspend fun removeFavorite(event: FavoriteEvent) {
        favDao.delete(event)
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventRepository? = null

        fun getInstance(
            apiService: ApiService,
            favDao: FavoriteEventDao,
            appExecutors: AppExecutors // Accept AppExecutors parameter here
        ): FavoriteEventRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventRepository(apiService, favDao, appExecutors) // Pass it to the constructor
            }.also { instance = it }
    }
}
