package sinaga.modern.event.ui

import ListEventsItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sinaga.modern.event.R
import sinaga.modern.event.data.response.EventListResponse
import sinaga.modern.event.data.retrofit.ApiConfig

class FinishedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: FinishedEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_finished, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_finished)
        progressBar = view.findViewById(R.id.progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(context)

        loadFinishedEvents()
        return view
    }

    private fun loadFinishedEvents() {
        progressBar.visibility = View.VISIBLE
        ApiConfig.getApiService().getEvents(active = 0, finished = 1).enqueue(object : Callback<EventListResponse> {
            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {
                if (response.isSuccessful) {
                    val finishedEvents = response.body()?.listEvents ?: emptyList()
                    adapter = FinishedEventAdapter(finishedEvents) { event ->
                        openEventDetailFragment(event) // Open detail fragment on item click
                    }
                    recyclerView.adapter = adapter
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle error (e.g., show a message)
            }
        })
    }

    private fun openEventDetailFragment(event: ListEventsItem) {
        val fragment = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("event_details", event) // Use putParcelable
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment) // Adjust based on your layout
            .addToBackStack(null)
            .commit()
    }

}
