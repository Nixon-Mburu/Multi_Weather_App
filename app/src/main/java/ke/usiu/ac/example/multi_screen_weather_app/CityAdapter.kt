package ke.usiu.ac.example.multi_screen_weather_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(
    private val cities: MutableList<String>,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityNameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.cityNameTextView.text = city
        holder.deleteButton.setOnClickListener {
            onDelete(city)
            cities.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = cities.size
}
