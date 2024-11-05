package sinaga.modern.event.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize  // Pastikan impor ini benar

@Entity
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val mediaCover: String,
    val description: String? = null,   // Tambahkan properti ini
    val cityName: String? = null,       // Tambahkan properti ini
    val ownerName: String? = null,      // Tambahkan properti ini
    val beginTime: String? = null,      // Tambahkan properti ini
    val endTime: String? = null,        // Tambahkan properti ini
    val quota: Int? = null,             // Tambahkan properti ini
    val registrants: Int? = null,       // Tambahkan properti ini
    val link: String? = null             // Tambahkan properti ini
) : Parcelable
