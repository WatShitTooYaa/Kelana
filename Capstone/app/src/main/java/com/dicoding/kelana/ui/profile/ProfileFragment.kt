package com.dicoding.kelana.ui.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.dicoding.kelana.R
import com.dicoding.kelana.custom.CustomDividerItem
import com.dicoding.kelana.data.DataProfileItem
import com.dicoding.kelana.data.UserModel
import com.dicoding.kelana.databinding.FragmentProfileBinding
import com.dicoding.kelana.db.UserPreference

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mUserPreference: UserPreference
    private var user = UserModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        mUserPreference = UserPreference(requireContext())

        setUser()

        profileViewModel.itemProfile.observe(viewLifecycleOwner){itemProfile ->
            setData(itemProfile)
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvFragmentProfile.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFragmentProfile.addItemDecoration(itemDecoration)
//        val drawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.custom_divider)
//        if (drawable != null) {
//            val itemDecoration = CustomDividerItem(requireContext(), drawable)
//            binding.rvFragmentProfile.addItemDecoration(itemDecoration)
//        }

        return root
    }

    private fun setUser(){
        user = mUserPreference.getUser()

        binding.tvProfileName.text = user.usename
        binding.tvProfileEmail.text = user.email
    }

    private fun setData(itemProfile: List<DataProfileItem>){
        val adapter = ProfileAdapter()
        adapter.submitList(itemProfile)
        binding.rvFragmentProfile.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}