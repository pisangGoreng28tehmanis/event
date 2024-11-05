package sinaga.modern.event.ui

import ListEventsItem
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sinaga.modern.event.R
import sinaga.modern.event.data.local.FavoriteEvent
import sinaga.modern.event.data.FavoriteEventRepository
import sinaga.modern.event.data.local.FavoriteEventRoomDatabase
import sinaga.modern.event.data.retrofit.ApiConfig
import sinaga.modern.event.data.Result
import sinaga.modern.event.utils.AppExecutors

class EventDetailFragment : Fragment() {

    private lateinit var event: ListEventsItem
    private var isFavorite: Boolean = false

    private lateinit var eventImage: ImageView
    private lateinit var eventName: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventLocation: TextView
    private lateinit var eventOwner: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventQuota: TextView
    private lateinit var eventRegistrants: TextView
    private lateinit var eventRemainingQuota: TextView
    private lateinit var openLinkButton: Button
    private lateinit var fabFavorite: FloatingActionButton

    private lateinit var favoriteRepository: FavoriteEventRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_detail, container, false)

        // Inisialisasi komponen UI
        eventImage = view.findViewById(R.id.image_event)
        eventName = view.findViewById(R.id.text_event_name)
        eventDescription = view.findViewById(R.id.text_event_description)
        eventLocation = view.findViewById(R.id.text_event_location)
        eventOwner = view.findViewById(R.id.text_event_owner)
        eventTime = view.findViewById(R.id.text_event_time)
        eventQuota = view.findViewById(R.id.text_event_quota)
        eventRegistrants = view.findViewById(R.id.text_event_registrants)
        eventRemainingQuota = view.findViewById(R.id.text_event_remaining_quota)
        openLinkButton = view.findViewById(R.id.button_open_link)
        fabFavorite = view.findViewById(R.id.fab_favorite)

        // Inisialisasi FavoriteEventRepository
        favoriteRepository = FavoriteEventRepository.getInstance(
            apiService = ApiConfig.getApiService(),
            favDao = FavoriteEventRoomDatabase.getDatabase(requireContext()).favoriteEventDao(),
            appExecutors = AppExecutors()
        )

        // Ambil data event dari arguments
        val event: ListEventsItem? = arguments?.getParcelable("event_details")
        event?.let {
            this.event = it

            // Tampilkan detail event
            Glide.with(requireContext())
                .load(it.mediaCover)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(eventImage)

            eventName.text = "Nama Acara: ${it.name ?: "N/A"}"
            eventDescription.text = Html.fromHtml(it.description ?: "", Html.FROM_HTML_MODE_COMPACT)
            eventLocation.text = "Lokasi: ${it.cityName ?: "N/A"}"
            eventOwner.text = "Pemilik: ${it.ownerName ?: "N/A"}"
            eventTime.text = "Waktu: ${it.beginTime} - ${it.endTime}"

            // Kuota dan registran
            eventQuota.text = "Kuota: ${it.quota ?: "N/A"}"
            eventRegistrants.text = "Registrants: ${it.registrants ?: 0}"

            // Hitung sisa kuota
            val remainingQuota = (it.quota ?: 0) - (it.registrants ?: 0)
            eventRemainingQuota.text = "Sisa Kuota: $remainingQuota"

            openLinkButton.setOnClickListener {
                event.link?.let { link ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                }
            }

            // Cek status favorit di database
            checkFavoriteStatus()
        }

        // Atur listener untuk FAB favorit
        fabFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon()

            // Launch a coroutine to toggle favorite status
            viewLifecycleOwner.lifecycleScope.launch {
                toggleFavoriteStatus()
            }
        }

        return view
    }

    // Fungsi untuk mengecek apakah event saat ini adalah favorit
    private fun checkFavoriteStatus() {
        favoriteRepository.getAllFavoriteEvents().observe(viewLifecycleOwner) { result ->
            if (result is Result.Success) {
                val favoriteEvents = result.data
                isFavorite = favoriteEvents.any { it.id == event.id.toString() }
                updateFavoriteIcon()
            }
        }
    }

    // Fungsi untuk memperbarui ikon favorit
    private fun updateFavoriteIcon() {
        val iconRes = if (isFavorite) {
            R.drawable.baseline_favorite_24 // Ikon hati terisi
        } else {
            R.drawable.baseline_favorite_border_24 // Ikon hati kosong
        }
        fabFavorite.setImageResource(iconRes)
    }

    // Fungsi untuk menambah atau menghapus favorit dari database
    private suspend fun toggleFavoriteStatus() {
        val favoriteEvent = FavoriteEvent(
            id = event.id.toString(),
            name = event.name ?: "",
            mediaCover = event.mediaCover ?: "",
            description = event.description ?: "",
            cityName = event.cityName ?: "",
            ownerName = event.ownerName ?: "",
            beginTime = event.beginTime ?: "",
            endTime = event.endTime ?: "",
            quota = event.quota ?: 0,
            registrants = event.registrants ?: 0,
            link = event.link ?: ""

        )

        if (isFavorite) {
            favoriteRepository.addFavorite(favoriteEvent)
        } else {
            favoriteRepository.removeFavorite(favoriteEvent)
        }
    }

    companion object {
        fun newInstance(event: ListEventsItem): EventDetailFragment {
            return EventDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("event_details", event)
                }
            }
        }
    }
}
