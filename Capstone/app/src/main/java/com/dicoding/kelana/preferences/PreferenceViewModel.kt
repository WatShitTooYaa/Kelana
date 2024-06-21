package com.dicoding.kelana.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kelana.R
import com.dicoding.kelana.data.DataPreferences

class PreferenceViewModel : ViewModel() {

    private val _preferences = MutableLiveData<List<DataPreferences>>()
    val preferences: LiveData<List<DataPreferences>> = _preferences

//    private val _selectedPref = MutableLiveData<List<DataPreferences>>()
//    val selectedPref : List<DataPreferences>

    init {
        dummyData()
    }

    private fun dummyData(){
        val dummyList = mutableListOf<DataPreferences>()
        val arrayCategories = listOf(
            "Taman Hiburan",
            "Cagar Alam",
            "Budaya",
            "Pusat Perbelanjaan",
            "Tempat Ibadah",
            "Bahari"
        )
        val arrayImageCategories = listOf(
            "https://asset-a.grid.id/crop/0x0:0x0/700x465/photo/2022/12/20/carousel-horses-carnival-merry-g-20221220085133.jpg",
            "https://awsimages.detik.net.id/community/media/visual/2019/12/26/68985cb9-8e44-4a1b-80db-8530f3efaa12_169.jpeg?w=1200",
            "https://cdn.idntimes.com/content-images/post/20190513/foto-kraton-holy-kartika-1269c3cd05fd9cc2abaa04e02dc4abd2_600x400.jpeg",
            "https://asset-2.tstatic.net/travel/foto/bank/images/ilustrasi_20171218_154505.jpg",
            "https://i0.wp.com/www.masjidnusantara.org/wp-content/uploads/2022/02/20220223-Istiqlal-Masjid-Simbol-Kemerdekaan-Indonesia.jpg?fit=900%2C600&ssl=1",
            "https://static.promediateknologi.id/crop/0x0:0x0/0x0/webp/photo/indizone/2023/04/19/r8s9kZd/jadi-pusat-konservasi-wisata-hutan-mangrove-kulonprogo-yang-punya-spot-foto-menarik46.jpg"

        )
        for (i in arrayCategories.indices) {
            val title = arrayCategories[i]
            val imageUrl = arrayImageCategories[i]
            dummyList.add(DataPreferences(title = title, image = imageUrl))
        }
        _preferences.value = dummyList
    }
}