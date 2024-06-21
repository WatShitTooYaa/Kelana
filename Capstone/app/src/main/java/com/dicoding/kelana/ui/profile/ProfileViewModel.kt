package com.dicoding.kelana.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataProfileItem

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val _itemProfile = MutableLiveData<List<DataProfileItem>>()
    val itemProfile : LiveData<List<DataProfileItem>> = _itemProfile

    init {
        setItemProfile()
    }

    private fun setItemProfile() {
        val dataDummy = mutableListOf<DataProfileItem>()
//        for (i in 1..4){
//            dataDummy.add(DataProfileItem(
//                R.drawable.baseline_person_24,
//                "Item $i"
//            ))
//        }
        dataDummy.add((DataProfileItem(R.drawable.baseline_settings_24, "Preference")))
        _itemProfile.value = dataDummy
    }
}