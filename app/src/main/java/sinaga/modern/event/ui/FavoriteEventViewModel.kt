package sinaga.modern.event.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sinaga.modern.event.data.FavoriteEventRepository
import sinaga.modern.event.data.Result
import sinaga.modern.event.data.local.FavoriteEvent

class FavoriteEventViewModel(private val favoriteEventRepository: FavoriteEventRepository) : ViewModel() {

    // Function to get all favorite events
    fun getFavoriteEvents(): LiveData<Result<List<FavoriteEvent>>> {
        return favoriteEventRepository.getAllFavoriteEvents()
    }

    // Function to remove a favorite event by ID
    fun removeFavoriteEvent(event: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.removeFavorite(event) // Pass the event object to remove it
        }
    }
}
