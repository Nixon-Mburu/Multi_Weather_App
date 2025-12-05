package ke.usiu.ac.example.multi_screen_weather_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var cityEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var favoritesButton: Button
    private val geocodingService = GeocodingService.create()
    private val weatherService = WeatherApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cityEditText = findViewById(R.id.cityEditText)
        searchButton = findViewById(R.id.searchButton)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        favoritesButton = findViewById(R.id.favoritesButton)

        searchButton.setOnClickListener {
            val city = cityEditText.text.toString().trim()
            if (city.isNotEmpty()) {
                searchCity(city)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    private fun searchCity(cityName: String) {
        loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val geocodeResponse = geocodingService.geocode(cityName)
                if (geocodeResponse.results.isEmpty()) {
                    loadingProgressBar.visibility = View.GONE
                    Snackbar.make(findViewById(R.id.main), "City not found", Snackbar.LENGTH_LONG).show()
                    return@launch
                }
                val location = geocodeResponse.results[0]
                val weatherResponse = weatherService.getWeather(location.latitude, location.longitude)
                loadingProgressBar.visibility = View.GONE

                val intent = Intent(this@MainActivity, DetailsActivity::class.java).apply {
                    putExtra("city", location.name)
                    putExtra("country", location.country)
                    putExtra("temp", weatherResponse.current.temperature.toInt())
                    putExtra("humidity", weatherResponse.current.humidity)
                    putExtra("weatherCode", weatherResponse.current.weatherCode)
                }
                startActivity(intent)
            } catch (e: Exception) {
                loadingProgressBar.visibility = View.GONE
                Snackbar.make(findViewById(R.id.main), "Error: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}