package com.dicoding.storyapp.story

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentDetailStoryBinding


class DetailStoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var desc: String? = null
    private var photo: String? = null

    private var _binding: FragmentDetailStoryBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME)
            desc = it.getString(DESC)
            photo = it.getString(PHOTO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)

        Glide.with(binding.root.context)
            .load(photo)
            .into(binding.ivDetailStory)
        binding.tvName.text = name
        binding.tvDesc.text = desc
        binding.closeFragment.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




    companion object {
        private const val NAME = "name"
        private const val DESC = "desc"
        private const val PHOTO = "photo"

        @JvmStatic
        fun newInstance(name: String, description: String, photo: String) =
            DetailStoryFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
                    putString(DESC, description)
                    putString(PHOTO, photo)
                }
            }
    }
}