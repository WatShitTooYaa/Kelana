package com.dicoding.kelana.data.response

import com.google.gson.annotations.SerializedName

data class PreferenceWisataResponse(

	@field:SerializedName("PreferenceWisataResponse")
	val preferenceWisataResponse: List<PreferenceWisataResponseItem>
)

data class PreferenceWisataResponseItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("Place_Name")
	val placeName: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("recommendation_id")
	val recommendationId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("place_id")
	val placeId: Int? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("long")
	val long: Double? = null,

	var distance: Float? = null
)
