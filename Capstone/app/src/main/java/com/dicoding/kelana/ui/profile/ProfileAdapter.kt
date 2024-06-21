package com.dicoding.kelana.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kelana.data.DataProfileItem
import com.dicoding.kelana.databinding.ItemProfileBinding

class ProfileAdapter : ListAdapter<DataProfileItem, ProfileAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val selectedItems = mutableListOf<DataProfileItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemProfile = getItem(position)
        holder.bind(itemProfile)
    }

    class MyViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemProfile: DataProfileItem) {
            binding.ivItemProfileIcon.setImageResource(itemProfile.icon)
            binding.tvItemProfile.text = itemProfile.name
//            Glide.with(binding.root.context)
//                .load(itemProfile.image)
//                .into(binding.ivHomeWisata)

            binding.root.setOnClickListener {
                when (bindingAdapterPosition) {
                    0 -> {
                        val intent = Intent(i)
                    }

                    1 -> {
                        // Handle click for the second item
                        Toast.makeText(
                            binding.root.context,
                            "Clicked on item 2",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        // Handle click for other items
                        Toast.makeText(
                            binding.root.context,
                            "Clicked on item $bindingAdapterPosition",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            // Update the background color based on whether the item is selected
        }
    }

    fun getSelectedItems(): List<DataProfileItem> {
        return selectedItems
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataProfileItem>() {
            override fun areItemsTheSame(oldItem: DataProfileItem, newItem: DataProfileItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataProfileItem, newItem: DataProfileItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}