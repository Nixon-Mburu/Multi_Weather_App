package ke.usiu.ac.example.multi_screen_weather_app

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.headerBg)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favorites = prefs.getStringSet("cities", setOf())?.toMutableList() ?: mutableListOf()

        val adapter = CityAdapter(favorites) { city ->
            val updatedFavorites = prefs.getStringSet("cities", setOf())?.toMutableSet() ?: mutableSetOf()
            updatedFavorites.remove(city)
            prefs.edit().putStringSet("cities", updatedFavorites).apply()
        }
        recyclerView.adapter = adapter
    }
}
