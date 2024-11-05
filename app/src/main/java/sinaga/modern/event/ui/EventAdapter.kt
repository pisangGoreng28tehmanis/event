package sinaga.modern.event.ui

import ListEventsItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sinaga.modern.event.R

class EventAdapter(
    private var events: List<ListEventsItem>, // Make this mutable
    private val onItemClick: (ListEventsItem) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventImage: ImageView = itemView.findViewById(R.id.image_event)
        private val eventCategory: TextView = itemView.findViewById(R.id.text_event_category)
        private val eventName: TextView = itemView.findViewById(R.id.text_event_name)
        private val eventDescription: TextView = itemView.findViewById(R.id.text_event_description)

        fun bind(event: ListEventsItem) {
            eventCategory.text = event.category
            eventName.text = event.name
            eventDescription.text = "${event.beginTime} - ${event.endTime}"

            Glide.with(itemView.context)
                .load(event.mediaCover)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(eventImage)

            itemView.setOnClickListener {
                onItemClick(event) // Call listener when item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<ListEventsItem>) {
        val diffCallback = EventDiffCallback(events, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        events = newEvents
        diffResult.dispatchUpdatesTo(this)
    }

}
