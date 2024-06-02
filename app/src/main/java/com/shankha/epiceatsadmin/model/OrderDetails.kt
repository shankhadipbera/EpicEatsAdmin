package com.shankha.epiceatsadmin.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OrderDetails() :Serializable {
    var userUid :String ?= null
    var userName :String ?= null
    var foodNames : MutableList<String> ? = null
    var foodImage : MutableList<String> ? = null
    var foodPrice : MutableList<String> ? = null
    var foodQuantities : MutableList<Int> ? = null
    var address :String ?= null
    var totalPrice :String ?= null
    var phoneNo :String ?= null
    var orderAccepted :Boolean = false
    var paymentReceived :Boolean =false
    var itemPushKey :String ?= null
    var currentTime :Long = 0
    var orderDispatch :Boolean = false


    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNo = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
        orderDispatch = parcel.readByte() != 0.toByte()
    }

     fun describeContents(): Int {
        TODO("Not yet implemented")
    }

  fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}