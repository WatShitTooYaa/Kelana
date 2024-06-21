package com.dicoding.kelana.preferences

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataPreferences
import com.dicoding.kelana.databinding.ItemPreferenceBinding

class PreferenceAdapter : ListAdapter<DataPreferences, PreferenceAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var selectedItems = mutableListOf<DataPreferences>()

    private var onSelectedItemsChanged: (() -> Unit)? = null

    fun setOnSelectedItemsChangedListener(listener: () -> Unit) {
        onSelectedItemsChanged = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemPreferenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val preference = getItem(position)
        holder.bind(preference, selectedItems, ::onItemSelected)
    }

    private fun onItemSelected(preference: DataPreferences) {
        // Check if the item is already selected
        if (selectedItems.contains(preference)) {
            // If it is, remove it from the selected items
            selectedItems.remove(preference)
        } else {
            // If it's not, clear the old selection and add the new one
            if (selectedItems.isNotEmpty()) {
                val oldSelected = selectedItems[0]
                selectedItems.clear()
                notifyItemChanged(currentList.indexOf(oldSelected))
            }
            selectedItems.add(preference)
        }
        // Notify that an item has changed
        notifyItemChanged(currentList.indexOf(preference))

        // Notify the listener
        onSelectedItemsChanged?.invoke()
    }


    class MyViewHolder(private val binding: ItemPreferenceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(preference: DataPreferences, selectedItems: MutableList<DataPreferences>, onItemSelected: (DataPreferences) -> Unit) {
            Glide.with(binding.root.context)
                .load(preference.image)
                .into(binding.image)
            binding.title.text = preference.title
//            binding.image.setImageResource(preference.image)


            // Update the background color based on whether the item is selected
            if (selectedItems.contains(preference)) {
                binding.cvItemPreference.setCardBackgroundColor(
                    ContextCompat.getColor(binding.cvItemPreference.context, R.color.menu_color_clicked)
                )
            } else {
                binding.cvItemPreference.setCardBackgroundColor(
                    ContextCompat.getColor(binding.cvItemPreference.context, R.color.menu_color)
                )
            }

            binding.cvItemPreference.setOnClickListener {
                if (!selectedItems.contains(preference)) {
                    onItemSelected(preference)
                }
            }


        }
    }

    fun getSelectedItems(): List<DataPreferences> {
        return selectedItems
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataPreferences>() {
            override fun areItemsTheSame(oldItem: DataPreferences, newItem: DataPreferences): Boolean {
                return oldItem == newItem // Assuming DataPreferences has an id field
            }

            override fun areContentsTheSame(oldItem: DataPreferences, newItem: DataPreferences): Boolean {
                return oldItem == newItem
            }
        }
    }
}

