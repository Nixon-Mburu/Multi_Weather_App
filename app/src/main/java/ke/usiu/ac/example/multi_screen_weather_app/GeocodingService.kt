package ke.usiu.ac.example.multi_screen_weather_app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    @SerializedName("results")
    val results: List<GeocodeResult>
)

data class GeocodeResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("country")
    val country: String
)

interface GeocodingService {
    @GET("search")
    suspend fun geocode(
        @Query("name") cityName: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodeResponse

    companion object {
        private const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"

        fun create(): GeocodingService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeocodingService::class.java)
        }
    }
}

