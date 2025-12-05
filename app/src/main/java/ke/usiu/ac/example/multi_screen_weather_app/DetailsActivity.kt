package ke.usiu.ac.example.multi_screen_weather_app

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val city = intent.getStringExtra("city") ?: "Unknown"
        val country = intent.getStringExtra("country") ?: ""
        val temp = intent.getIntExtra("temp", 0)
        val humidity = intent.getIntExtra("humidity", 0)
        val weatherCode = intent.getIntExtra("weatherCode", 0)

        val condition = getWeatherDescription(weatherCode)
        val weatherEmoji = getWeatherEmoji(weatherCode)

        findViewById<TextView>(R.id.cityTextView).text = "$city, $country"
        findViewById<TextView>(R.id.tempTextView).text = "$temp°C"
        findViewById<TextView>(R.id.conditionTextView).text = condition
        findViewById<TextView>(R.id.humidityTextView).text = "Humidity: $humidity%"
        findViewById<TextView>(R.id.weatherIcon).text = weatherEmoji

        findViewById<Button>(R.id.addToFavoritesButton).setOnClickListener {
            addToFavorites(city)
            Toast.makeText(this, "$city added to favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getWeatherDescription(code: Int): String = when (code) {
        0 -> "Clear sky"
        1, 2 -> "Mostly clear"
        3 -> "Overcast"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rain"
        71, 73, 75 -> "Snow"
        77 -> "Snow grains"
        80, 82, 85 -> "Showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Unknown"
    }

    private fun getWeatherEmoji(code: Int): String = when (code) {
        0 -> "☀️"
        1, 2 -> "🌤️"
        3 -> "☁️"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌧️"
        61, 63, 65 -> "🌧️"
        71, 73, 75, 77 -> "❄️"
        80, 82, 85 -> "⛈️"
        95, 96, 99 -> "⚡"
        else -> "🌍"
    }

    private fun addToFavorites(city: String) {
        val prefs = getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favorites = prefs.getStringSet("cities", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        favorites.add(city)
        prefs.edit().putStringSet("cities", favorites).apply()
    }
}
