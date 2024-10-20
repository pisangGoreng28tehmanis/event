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

class UpcomingEventFragmentAdapter(private val events: List<ListEventsItem>) :
    RecyclerView.Adapter<UpcomingEventFragmentAdapter.UpcomingViewHolder>() {

    inner class UpcomingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.text_event_name)
        private val eventOwner: TextView = itemView.findViewById(R.id.text_event_owner)
        private val eventImage: ImageView = itemView.findViewById(R.id.image_event)

        fun bind(event: ListEventsItem) {
            eventName.text = event.name
            eventOwner.text = event.ownerName // Pastikan ada field owner di data event
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(eventImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_event, parent, false)
        return UpcomingViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}
