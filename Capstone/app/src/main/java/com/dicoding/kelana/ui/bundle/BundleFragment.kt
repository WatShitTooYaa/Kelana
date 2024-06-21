package com.dicoding.kelana.ui.bundle

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataBundle
import com.dicoding.kelana.databinding.FragmentBundleBinding
import com.dicoding.kelana.ui.bundle.sheet.MyBottomSheetDialogFragment

class BundleFragment : Fragment() {

    private var _binding: FragmentBundleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val bundleViewModel =
            ViewModelProvider(this)[BundleViewModel::class.java]

        _binding = FragmentBundleBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnBundleFilter.setOnClickListener {
            val bottomSheetDialog = MyBottomSheetDialogFragment()
            bottomSheetDialog.show(childFragmentManager, "SideSheetDialog")
        }

        bundleViewModel.bundleWisata.observe(viewLifecycleOwner){
            if (it != null){
                switchLayout(true)
                setData(it)
            }
            else {
                switchLayout(false)
            }
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvFragmentBundle.layoutManager = layoutManager

        return root
    }

    private fun switchLayout(isDataNotNull : Boolean){
        if (isDataNotNull){
            binding.rvFragmentBundle.visibility = View.VISIBLE
            binding.tvFragmentBundleDesc.visibility = View.GONE
        }
        else {
            binding.rvFragmentBundle.visibility = View.GONE
            binding.tvFragmentBundleDesc.visibility = View.VISIBLE
        }
    }

    private fun setData(data: List<DataBundle>){
        val adapter = BundleAdapter()
        adapter.submitList(data)
        binding.rvFragmentBundle.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}