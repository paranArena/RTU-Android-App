package com.rtu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CreateProductResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: ProductModel
)

@Parcelize
data class ProductModel(
    val id: Int,
    val name: String,
    val category: String,
    val location: LocationModel,
    val fifoRentalPeriod: Int,
    val reserveRentalPeriod: Int,
    val price: Int,
    val caution: String,
    val imagePath: String,
    val item: List<ItemModel>
): Parcelable

@Parcelize
data class LocationModel(
    val name: String,
    val latitude: Double,
    val longitude: Double
): Parcelable

@Parcelize
data class ItemModel(
    val id: Int,
    val numbering: Int,
    val rentalPolicy: String,
    val rentalDto: String
): Parcelable