package sinaga.modern.event.ui

import ListEventsItem
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
import com.bumptech.glide.Glide
import sinaga.modern.event.R

class EventDetailFragment : Fragment() {

    private lateinit var event: ListEventsItem

    private lateinit var eventImage: ImageView
    private lateinit var eventName: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventLocation: TextView
    private lateinit var eventOwner: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventQuota: TextView
    private lateinit var eventRegistrants: TextView
    private lateinit var eventRemainingQuota: TextView // New TextView for remaining quota
    private lateinit var openLinkButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_detail, container, false)

        eventImage = view.findViewById(R.id.image_event)
        eventName = view.findViewById(R.id.text_event_name)
        eventDescription = view.findViewById(R.id.text_event_description)
        eventLocation = view.findViewById(R.id.text_event_location)
        eventOwner = view.findViewById(R.id.text_event_owner)
        eventTime = view.findViewById(R.id.text_event_time)
        eventQuota = view.findViewById(R.id.text_event_quota)
        eventRegistrants = view.findViewById(R.id.text_event_registrants)
        eventRemainingQuota = view.findViewById(R.id.text_event_remaining_quota) // Initialize the new TextView
        openLinkButton = view.findViewById(R.id.button_open_link)

        // Get the event details from arguments
        val event: ListEventsItem? = arguments?.getParcelable("event_details")

        event?.let {
            // Set event details
            Glide.with(requireContext())
                .load(it.mediaCover)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(eventImage)

            eventName.text = "Nama Acara: ${it.name ?: "N/A"}"
            eventDescription.text = Html.fromHtml(it.description ?: "", Html.FROM_HTML_MODE_COMPACT) // Display HTML content
            eventLocation.text = "Lokasi: ${it.cityName ?: "N/A"}"
            eventOwner.text = "Pemilik: ${it.ownerName ?: "N/A"}"
            eventTime.text = "Waktu: ${it.beginTime} - ${it.endTime}"

            // Concatenate quota and registrants
            val quotaText = it.quota?.let { quota -> "Kuota: $quota" } ?: "Kuota: N/A"
            val registrantsText = it.registrants?.let { registrants -> "Registrants: $registrants" } ?: "Registrants: 0"
            eventQuota.text = quotaText
            eventRegistrants.text = registrantsText

            // Calculate remaining quota and set the text
            val remainingQuota = (it.quota ?: 0) - (it.registrants ?: 0)
            eventRemainingQuota.text = "Sisa Kuota: $remainingQuota"

            openLinkButton.setOnClickListener {
                // Open the event link (if available)
                event.link?.let { link ->
                    // Use Intent to open the link
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                }
            }
        }

        return view
    }

    companion object {
        fun newInstance(event: ListEventsItem): EventDetailFragment {
            return EventDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("event_details", event) // Ensure you're using putParcelable
                }
            }
        }
    }
}
