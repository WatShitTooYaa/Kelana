package com.dicoding.kelana.ui.bundle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataBundle
import com.dicoding.kelana.databinding.ItemBundleBinding

class BundleAdapter : ListAdapter<DataBundle, BundleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val selectedItems = mutableListOf<DataBundle>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemBundleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wisata = getItem(position)
        holder.bind(wisata)
    }

    class MyViewHolder(private val binding: ItemBundleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata: DataBundle) {
            binding.tvBundleWisataName.text = wisata.name
            binding.ivBundleWisata.setImageResource(R.drawable.pantai)
            binding.tvBundleWisataLoc.text = wisata.location
            binding.tvBundleWisataPrice.text = formatNumbers(wisata.price ?: "")
            binding.tvBundleWisataDuration.text = wisata.duration
//            Glide.with(binding.root.context)
//                .load(wisata.image)
//                .into(binding.ivHomeWisata)

            // Update the background color based on whether the item is selected
        }
    }

    fun getSelectedItems(): List<DataBundle> {
        return selectedItems
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataBundle>() {
            override fun areItemsTheSame(oldItem: DataBundle, newItem: DataBundle): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataBundle, newItem: DataBundle): Boolean {
                return oldItem == newItem
            }
        }

        fun formatNumbers(number: String): String {
            val num =  number.toLongOrNull()?.let {
                java.text.DecimalFormat("#,###").format(it)
            } ?: number

            return "Rp. $num"
        }
    }
}