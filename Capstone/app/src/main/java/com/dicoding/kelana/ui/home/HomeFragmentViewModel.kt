package com.dicoding.kelana.ui.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kelana.data.api.ApiConfig
import com.dicoding.kelana.data.response.PreferenceWisataResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel : ViewModel() {

    private val _lastLocation = MutableLiveData<String>()
    val lastLocation: LiveData<String> = _lastLocation

    private val _dataWisata = MutableLiveData<List<PreferenceWisataResponseItem>>()
    val dataWisata: LiveData<List<PreferenceWisataResponseItem>> = _dataWisata

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private var currentLocation: Location? = null


    fun getWisata(token: String, preference: String) {
        val client = ApiConfig.getWisataApiService(token).fetchWisata(preference)

        client.enqueue(object : Callback<List<PreferenceWisataResponseItem>> {
            override fun onResponse(
                call: Call<List<PreferenceWisataResponseItem>>,
                response: Response<List<PreferenceWisataResponseItem>>
            ) {
                val responseBody = response.body()
                Log.d("HomeViewModel", "onResponse: $responseBody ")
                if (response.isSuccessful && responseBody != null){
                    responseBody.let {  _dataWisata.value = it }
                    _status.value = "sukses"
                    sortData("name")
                }
                else {
//                    getDataWisata()
                    _status.value = "gagal di response"
                }
            }

            override fun onFailure(call: Call<List<PreferenceWisataResponseItem>>, t: Throwable) {
//                getDataWisata()
                _status.value = "gagal saat get"
            }
        })
    }

    fun onSortOptionSelected(criteria: String) {
        sortData(criteria)
    }

    fun sortData(criteria: String) {
        val sortedList = when (criteria) {
            "distance" -> {
                _lastLocation.value?.let { location ->
                    val locParts = location.split(",")
                    val currentLat = locParts[0].toDouble()
                    val currentLong = locParts[1].toDouble()

                    _dataWisata.value?.sortedBy {
                        it.lat?.let { lat ->
                            it.long?.let { long ->
                                calculateDistance(currentLat, currentLong,
                                    lat, long
                                )
                            }
                        }
                    }
                } ?: _dataWisata.value
            }
            else -> {
                _dataWisata.value?.sortedBy { it.placeName }
            }
        }
        sortedList?.let { _dataWisata.value = it }
//        _dataWisata.value = sortedList
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    fun setLocation(location: String?) {
        location?.let { _lastLocation.value = it}
    }

    fun sortByName() {
        _dataWisata.value = _dataWisata.value?.sortedBy { it.placeName }
    }

    fun sortByDistance() {
        _dataWisata.value = _dataWisata.value?.sortedBy { it.distance }
    }
}