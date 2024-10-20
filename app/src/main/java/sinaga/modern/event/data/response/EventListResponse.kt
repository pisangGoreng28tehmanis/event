package sinaga.modern.event.data.response

import ListEventsItem
import com.google.gson.annotations.SerializedName

data class EventListResponse(
    @field:SerializedName("listEvents")
    val listEvents: List<ListEventsItem> = listOf(), // Daftar acara

    @field:SerializedName("error")
    val error: Boolean? = null, // Status error

    @field:SerializedName("message")
    val message: String? = null // Pesan tambahan
)
