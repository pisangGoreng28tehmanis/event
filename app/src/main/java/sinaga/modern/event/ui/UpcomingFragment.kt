package sinaga.modern.event.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sinaga.modern.event.R
import sinaga.modern.event.data.response.EventListResponse
import sinaga.modern.event.data.retrofit.ApiConfig

class UpcomingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_upcoming)
        progressBar = view.findViewById(R.id.progress_bar)

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        loadEvents()
        return view
    }

    private fun loadEvents() {
        progressBar.visibility = View.VISIBLE
        ApiConfig.getApiService().getEvents(active = 1, finished = 0)
            .enqueue(object : Callback<EventListResponse> {
                override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                    if (response.isSuccessful) {
                        val events = response.body()?.listEvents ?: emptyList()
                        if (events.isEmpty()) {
                            Toast.makeText(context, "No events found", Toast.LENGTH_SHORT).show()
                        } else {
                            // Menggunakan UpcomingEventFragmentAdapter
                            val adapter = UpcomingEventAdapter(events) { selectedEvent ->
                                // Navigasi ke EventDetailFragment
                                val fragment = EventDetailFragment.newInstance(selectedEvent)
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, fragment)
                                    .addToBackStack(null) // Tambahkan ke back stack
                                    .commit()
                            }
                            recyclerView.adapter = adapter
                        }
                    } else {
                        Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UpcomingFragment", "Error: ${t.message}")
                }
            })
    }
}
