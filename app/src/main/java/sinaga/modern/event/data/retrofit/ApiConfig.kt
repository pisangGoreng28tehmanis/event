package sinaga.modern.event.data.retrofit

import ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://event-api.dicoding.dev/") // Base URL yang disesuaikan
                .addConverterFactory(GsonConverterFactory.create()) // Converter untuk mengubah JSON menjadi objek
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java) // Mengembalikan instance ApiService
        }
    }
}
