package com.rtu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class RequestRentResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: RentResponseDetail
)

@Parcelize
data class RentResponseDetail(
    val id: Int,
    val numbering: Int,
    val rentalPolicy: String,
    val rentalInfo: RentalInfo
): Parcelable

data class RentResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: RentInfo
)

@Parcelize
data class RentInfo(
    val entityName: String,
    val id: Int
): Parcelable


data class MyRentalResponse(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<RentDetail>
)

@Parcelize
data class RentDetail(
    val id: Int,
    val numbering: Int,
    val name: String,
    val clubId: Int,
    val clubName: String,
    val imagePath: String,
    val rentalPolicy: String,
    val rentalInfo: RentalInfo
): Parcelable

data class ManageRentModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<ManageRentData>
)

@Parcelize
data class ManageRentData(
    val memberName: String,
    val id: Int,
    val numbering: Int,
    val name: String,
    val clubId: Int,
    val clubName: String,
    val imagePath: String,
    val rentalPolicy: String,
    val rentalInfo: RentalInfo
): Parcelable

data class ReturnModel(
    val statusCode: Int,
    val responseMessage: String,
    val data: List<ReturnLog>
)

@Parcelize
data class ReturnLog(
    val id: Int,
    val thumbnailPath: String,
    val productName: String,
    val memberName: String,
    val rentDate: String,
    val expDate: String,
    val returnDate: String,
    val rentalStatus: String,
    val numbering: Int
): Parcelable