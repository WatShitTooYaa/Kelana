package com.dicoding.kelana.data

data class DataWisata (
    var id: String? = null,
    var name: String,
    var desc: String,
    var image: String,
    var rating: Double? = null,
    var price: String,
    var location: String,
    var pref: String,
    var lat: Double? = 0.0,
    var long: Double? = 0.0,
)