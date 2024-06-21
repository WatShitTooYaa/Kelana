package com.dicoding.kelana.ui.bundle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kelana.data.DataBundle

class BundleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _bundleWisata = MutableLiveData<List<DataBundle>>()
    val bundleWisata: LiveData<List<DataBundle>> = _bundleWisata

//    init {
//        getDummyData()
//    }

    fun getDummyData(){
        val dataDummy = mutableListOf<DataBundle>()
        for (i in 1..10){
            dataDummy.add(
                DataBundle(
                    id = i.toString(),
                    name = "wisata $i",
                    duration = "${(i % 3)+1} day",
                    price = "${(8000000 + i * 100000)}",
                    location = if (i%3 == 0){
                            "Semarang"
                        }
                        else if (i % 3 == 1){
                            "Bandung"
                        }
                        else {
                            "Banyuwangi"
                        }
                )
            )
        }
        _bundleWisata.value = dataDummy
    }
}