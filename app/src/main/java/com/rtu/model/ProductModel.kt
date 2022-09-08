package com.rtu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CreateProductResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: ProductModel
)

data class GetProductResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: ProductInfo
)

@Parcelize
data class ProductInfo(
    val id: Int,
    val name: String,
    val category: String,
    val location: LocationModel,
    val fifoRentalPeriod: Int,
    val reserveRentalPeriod: Int,
    val price: Int,
    val caution: String,
    val imagePath: String,
    val items: List<ItemsModel>
): Parcelable

@Parcelize
data class ItemsModel(
    val id: Int,
    val numbering: Int,
    val rentalPolicy: String,
    val rentalInfo: RentalInfo
): Parcelable

@Parcelize
data class RentalInfo(
    val rentalStatus: String?,
    val rentDate: String,
    val expDate: String?
): Parcelable

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
    val items: List<ItemModel>
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

data class GetProductModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<ProductDetail>
)

@Parcelize
data class ProductDetail(
    val id: Int,
    val name: String,
    val clubId: Int,
    val clubName: String,
    val category: String,
    val imagePath: String,
    val left: Int,
    val max: Int
): Parcelable