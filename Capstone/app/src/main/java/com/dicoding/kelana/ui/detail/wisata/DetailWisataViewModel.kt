package com.dicoding.kelana.ui.detail.wisata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kelana.data.DataWisata
import com.dicoding.kelana.data.api.ApiConfig
import com.dicoding.kelana.data.response.DetailWisataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailWisataViewModel : ViewModel() {
    private val _wisata = MutableLiveData<DetailWisataResponse>()
    val wisata : LiveData<DetailWisataResponse> = _wisata

//    init {
//        getDataWisata()
//    }

    fun getDataWisata(token: String, placeId: String){
        val client = ApiConfig.getWisataApiService(token).getDetailWisata(placeId)
        client.enqueue(object : Callback<DetailWisataResponse>{
            override fun onResponse(
                call: Call<DetailWisataResponse>,
                response: Response<DetailWisataResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    responseBody.let { _wisata.value = it }
                }
                else {
                    //
                }
            }

            override fun onFailure(call: Call<DetailWisataResponse>, t: Throwable) {
                //
            }
        })
    }

//    private fun getDataWisata(){
//        val dataDummy = DataWisata(
//            id = null,
//            name = "Jl. Malioboro",
//            desc = "Jalan Malioboro (bahasa Jawa: ꦢꦭꦤ꧀ꦩꦭꦶꦪꦧꦫ, translit. Dalan Maliabara) adalah salah satu kawasan jalan dari tiga jalan di Kota Yogyakarta yang membentang dari Tugu Yogyakarta hingga ke persimpangan Titik Nol Kilometer Yogyakarta.\n" +
//                    "\n" +
//                    "Secara keseluruhan, kawasan Malioboro terdiri atas Jalan Margo Utomo, Jalan Malioboro, dan Jalan Margo Mulyo.",
//            image = "https://upload.wikimedia.org/wikipedia/commons/f/f0/Malioboro_Street%2C_Yogyakarta.JPG",
//            rating = 4.5,
//            price = "10.000",
//            location = "Sosromenduran, Gedong Tengen, Kota Yogyakarta, Daerah Istimewa Yogyakarta",
//            pref = "Sejarah",
//            lat = -7.7922069573717865,
//            long = 110.36719914562313,
//        )
//        _wisata.value = dataDummy
//    }
}