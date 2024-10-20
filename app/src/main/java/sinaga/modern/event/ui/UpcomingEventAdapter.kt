package sinaga.modern.event.ui

import ListEventsItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sinaga.modern.event.R

class UpcomingEventAdapter(
    private val events: List<ListEventsItem>,
    private val onEventClick: (ListEventsItem) -> Unit // Callback for item click
) : RecyclerView.Adapter<UpcomingEventAdapter.UpcomingViewHolder>() {

    inner class UpcomingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.text_event_name)
        private val eventDescription: TextView = itemView.findViewById(R.id.text_event_description)
        private val eventImage: ImageView = itemView.findViewById(R.id.image_event)
        private val eventCategory: TextView = itemView.findViewById(R.id.text_event_category)

        // Binds the event data to the view
        fun bind(event: ListEventsItem) {
            eventCategory.text = event.category
            eventName.text = event.name
            eventDescription.text = "${event.beginTime} - ${event.endTime}"

            // Load the image using Glide
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                .error(R.drawable.error_image) // Error image if loading fails
                .into(eventImage)

            // Set click listener for the item
            itemView.setOnClickListener {
                onEventClick(event) // Call the click listener with the event
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return UpcomingViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        // Bind the event to the view holder
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size // Return the size of the events list
}
