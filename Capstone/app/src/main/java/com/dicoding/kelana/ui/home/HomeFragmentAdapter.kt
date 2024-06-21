package com.dicoding.kelana.ui.home

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.kelana.data.response.PreferenceWisataResponseItem
import com.dicoding.kelana.databinding.ItemHomeBinding

class HomeFragmentAdapter(
    private var currentLocation: Location,
    private val viewModel: HomeFragmentViewModel // Tambahkan viewModel sebagai parameter
) : ListAdapter<PreferenceWisataResponseItem, HomeFragmentAdapter.MyViewHolder>(DIFF_CALLBACK) {

//    private val selectedItems = mutableListOf<PreferenceWisataResponseItem>()
    private var onClickListener: OnClickListener? = null
//    private var currentLocation: Location? = null // Lokasi saat ini

//    fun setCurrentLocation(location: Location?) {
//        currentLocation = location
//    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wisata = getItem(position)
        val distance = wisata.lat?.let {
            wisata.long?.let { it1 ->
                calculateDistance(currentLocation.latitude, currentLocation.longitude,
                    it, it1
                )
            }
        }
        wisata.distance = distance
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position, wisata)
        }
        holder.bind(wisata)
    }

    class MyViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata: PreferenceWisataResponseItem) {
            binding.tvHomeWisataName.text = wisata.placeName
            binding.tvHomeWisataLoc.text = wisata.city
            binding.tvHomeWisataPrice.text = formatNumber(wisata.price.toString())
//            binding.ivHomeWisata.setImageResource(R.drawable.pantai)
            binding.tvHomeWisataDistance.text = String.format("%.2f km",
                wisata.distance?.div(1000) ?: "null"
            )
            Glide.with(binding.root.context)
                .load(wisata.image)
                .into(binding.ivHomeWisata)

//            currentLocation?.let { currentLoc ->
//                val wisataLoc = Location("").apply {
////                    latitude = wisata.lat
//                    wisata.lat?.let { latitude = it }
////                    longitude = wisata.jsonMemberLong
//                    wisata.long?.let { longitude = it }
//                }
//                val distance = currentLoc.distanceTo(wisataLoc) // Jarak dalam meter
//                val distanceInKm = distance / 1000 // Konversi ke kilometer
//
//                binding.tvHomeWisataDistance.text = String.format("%.2f km", distanceInKm)
//            }
        }
    }

    // Fungsi untuk menghitung jarak
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    fun setOnClickListener(listener: OnClickListener?){
        onClickListener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: PreferenceWisataResponseItem)
    }

    fun updateCurrentLocation(location: Location) {
        currentLocation = location
        notifyDataSetChanged() // Refresh adapter
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PreferenceWisataResponseItem>() {
            override fun areItemsTheSame(oldItem: PreferenceWisataResponseItem, newItem: PreferenceWisataResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PreferenceWisataResponseItem, newItem: PreferenceWisataResponseItem): Boolean {
                return oldItem == newItem
            }
        }

        fun formatNumber(number: String): String {
            val num =  number.toLongOrNull()?.let {
                java.text.DecimalFormat("#,###").format(it)
            } ?: number

            return "Rp. $num"
        }
    }
}
