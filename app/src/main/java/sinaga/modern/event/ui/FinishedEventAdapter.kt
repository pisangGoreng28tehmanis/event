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

class FinishedEventAdapter(
    private val events: List<ListEventsItem>,
    private val onItemClick: (ListEventsItem) -> Unit // Add this line
) : RecyclerView.Adapter<FinishedEventAdapter.FinishedViewHolder>() {

    inner class FinishedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                onItemClick(event) // Call the click listener with the event
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_finished_event, parent, false)
        return FinishedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}
