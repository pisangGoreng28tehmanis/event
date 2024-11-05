package sinaga.modern.event.ui

import ListEventsItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sinaga.modern.event.R
import sinaga.modern.event.data.Result
import sinaga.modern.event.ui.ViewModelFactory

class FavoriteFragment : Fragment() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var viewModel: FavoriteEventViewModel
    private var scrollPosition: Int = 0 // Variabel untuk menyimpan posisi scroll

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore scroll position if available
        scrollPosition = savedInstanceState?.getInt("scroll_position") ?: 0

        // Initialize ViewModelFactory and ViewModel
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewModel = viewModels<FavoriteEventViewModel> { factory }.value

        // Initialize Adapter with an empty list initially
        eventAdapter = EventAdapter(emptyList()) { event ->
            openEventDetailFragment(event)
        }

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFavorites)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = eventAdapter

        // Observe data from ViewModel
        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show a loading indicator if needed
                }
                is Result.Success -> {
                    // Map FavoriteEvent to ListEventsItem
                    val eventsList = result.data.map { favoriteEvent ->
                        ListEventsItem(
                            id = favoriteEvent.id,
                            name = favoriteEvent.name,
                            mediaCover = favoriteEvent.mediaCover,
                            description = favoriteEvent.description ?: "",  // Tambahkan ini
                            cityName = favoriteEvent.cityName ?: "",        // Tambahkan ini
                            ownerName = favoriteEvent.ownerName ?: "",      // Tambahkan ini
                            beginTime = favoriteEvent.beginTime ?: "",      // Tambahkan ini
                            endTime = favoriteEvent.endTime ?: "",          // Tambahkan ini
                            quota = favoriteEvent.quota ?: 0,               // Tambahkan ini
                            registrants = favoriteEvent.registrants ?: 0,   // Tambahkan ini
                            link = favoriteEvent.link ?: ""

                        )
                    }

                    // Update adapter with new data
                    eventAdapter.updateData(eventsList) // Method to update adapter data
                    recyclerView.scrollToPosition(scrollPosition) // Restore RecyclerView scroll position
                }
                is Result.Error -> {
                    // Show error message
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openEventDetailFragment(event: ListEventsItem) {
        val fragment = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("event_details", event)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save scroll position of RecyclerView
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvFavorites)
        scrollPosition = (recyclerView?.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: 0
        outState.putInt("scroll_position", scrollPosition)
    }
}
