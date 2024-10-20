import android.content.IntentSender.OnFinished
import retrofit2.Call
import retrofit2.http.*
import sinaga.modern.event.data.response.EventDetailResponse
import sinaga.modern.event.data.response.EventListResponse

interface ApiService {

    @GET("events")
    fun getEvents(
        @Query("active") active: Int = 1,
        @Query("finished") finished: Int = 0,
        @Query("q") query: String? = null,
        @Query("limit") limit: Int = 5
    ): Call<EventListResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<EventDetailResponse>


}
