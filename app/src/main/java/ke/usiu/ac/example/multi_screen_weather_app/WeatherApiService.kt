package ke.usiu.ac.example.multi_screen_weather_app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("relative_humidity_2m")
    val humidity: Int
)

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code,relative_humidity_2m",
        @Query("temperature_unit") tempUnit: String = "celsius"
    ): WeatherResponse

    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/"

        fun create(): WeatherApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}
