package sinaga.modern.event.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<FavoriteEvent>)

    @Update
    suspend fun update(favoriteEvent: FavoriteEvent)

    @Query("DELETE FROM FavoriteEvent")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(events: FavoriteEvent)

    @Query("SELECT * from FavoriteEvent ORDER BY id ASC")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>
}