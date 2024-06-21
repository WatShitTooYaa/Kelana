package com.dicoding.storyapp.story

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityStoryBinding
import com.dicoding.storyapp.databinding.ItemRowStoryBinding

class ListStoryAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val containerId: Int) : ListAdapter<ListStoryItem ,ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener{
            showDetailFragment(context, fragmentManager,containerId, story)
        }
    }

    class MyViewHolder(private val binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
//        val fragmentContainer: FrameLayout = itemView.findViewById(R.id.fragment_container)
        fun bind(story : ListStoryItem) {
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imgItemPhoto)
            binding.tvItemName.text = story.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun showDetailFragment(context: Context, fragmentManager: FragmentManager, containerId: Int, story: ListStoryItem) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        val fragmentContainer = i
        val activity = context as Activity
        val fragmentContainer = activity.findViewById<FrameLayout>(containerId)
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        fragmentContainer.visibility = View.VISIBLE
        fragmentContainer.startAnimation(fadeInAnimation)

        val detailFragment = DetailStoryFragment.newInstance(story.name, story.description, story.photoUrl)

        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_pop_enter,
                R.anim.fragment_pop_exit)
            .replace(containerId, detailFragment)
            .addToBackStack(null) // Optional: Remove this line if you don't want to add it to back stack
            .commit()
    }


}