package sinaga.modern.event.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sinaga.modern.event.R
import sinaga.modern.event.data.retrofit.ApiConfig
import sinaga.modern.event.data.response.EventListResponse

class DefaultFragment : Fragment() {

    private lateinit var upcomingRecyclerView: RecyclerView
    private lateinit var finishedRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var upcomingEventAdapter: UpcomingEventAdapter
    private lateinit var finishedEventAdapter: FinishedEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_default, container, false)

        upcomingRecyclerView = view.findViewById(R.id.recycler_view_upcoming)
        finishedRecyclerView = view.findViewById(R.id.recycler_view_finished)
        progressBar = view.findViewById(R.id.progress_bar)

        // Setup RecyclerView
        upcomingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        finishedRecyclerView.layoutManager = LinearLayoutManager(context)

        // Start loading data
        loadEvents()

        return view
    }

    private fun loadEvents() {
        progressBar.visibility = View.VISIBLE

        // Call API to get active events
        ApiConfig.getApiService().getEvents(active = 1, finished = 0).enqueue(object : Callback<EventListResponse> {
            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {
                    val upcomingEvents = response.body()?.listEvents ?: emptyList()
                    upcomingEventAdapter = UpcomingEventAdapter(upcomingEvents) { selectedEvent ->
                        // Navigate to EventDetailFragment
                        val fragment = EventDetailFragment.newInstance(selectedEvent) // Ensure selectedEvent is of type ListEventsItem
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment)
                            .addToBackStack(null) // Add to back stack
                            .commit()
                    }
                    upcomingRecyclerView.adapter = upcomingEventAdapter
                }
                checkIfLoadingFinished()
            }

            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle error here
            }
        })

        // Call API to get finished events
        ApiConfig.getApiService().getEvents(active = 0, finished = 1).enqueue(object : Callback<EventListResponse> {
            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {
                    val finishedEvents = response.body()?.listEvents ?: emptyList()
                    finishedEventAdapter = FinishedEventAdapter(finishedEvents) { selectedEvent ->
                        // Navigate to EventDetailFragment for finished events
                        val fragment = EventDetailFragment.newInstance(selectedEvent)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    finishedRecyclerView.adapter = finishedEventAdapter
                }
                checkIfLoadingFinished()
            }

            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle error here
            }
        })
    }

    // Function to check if all data has finished loading
    private fun checkIfLoadingFinished() {
        if (upcomingRecyclerView.adapter != null && finishedRecyclerView.adapter != null) {
            progressBar.visibility = View.GONE
        }
    }
}
