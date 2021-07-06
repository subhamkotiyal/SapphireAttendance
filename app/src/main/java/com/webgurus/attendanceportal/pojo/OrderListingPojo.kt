package com.webgurus.attendanceportal.pojo

import android.os.Parcel
import android.os.Parcelable

data class OrderListingPojo(
    val `data`: List<OrderData>,
    val message: String,
    val status: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<OrderData>().apply {
            parcel.readList(this as List<*>,
                OrderListingPojo::class.java.classLoader)
        },
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderListingPojo> {
        override fun createFromParcel(parcel: Parcel): OrderListingPojo {
            return OrderListingPojo(parcel)
        }

        override fun newArray(size: Int): Array<OrderListingPojo?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrderData (
    val address: String,
    val customer_name: String,
    val created_by_id: Int,
    val date: String,
    val id: Int,
    val instructions: String,
    val is_payment_complete: Int,
    val payment_pending: Int,
    val payment_received: Int,
    val payment_type: Int,
    val status: Int,
    val total_price: String,
    val variant: ArrayList<OrderVariant>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        TODO("variant")
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(customer_name)
        parcel.writeInt(created_by_id)
        parcel.writeString(date)
        parcel.writeInt(id)
        parcel.writeString(instructions)
        parcel.writeInt(is_payment_complete)
        parcel.writeInt(payment_pending)
        parcel.writeInt(payment_received)
        parcel.writeInt(payment_type)
        parcel.writeInt(status)
        parcel.writeString(total_price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderData> {
        override fun createFromParcel(parcel: Parcel): OrderData {
            return OrderData(parcel)
        }

        override fun newArray(size: Int): Array<OrderData?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrderVariant (
    val id: Int,
    val max_price: String,
    val min_price: String,
    val product_name: String,
    val product_price: String,
    val product_unit: String,
    val quantity: Int,
    val quoted_price: String,
    val total_amount: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(max_price)
        parcel.writeString(min_price)
        parcel.writeString(product_name)
        parcel.writeString(product_price)
        parcel.writeString(product_unit)
        parcel.writeInt(quantity)
        parcel.writeString(quoted_price)
        parcel.writeString(total_amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderVariant> {
        override fun createFromParcel(parcel: Parcel): OrderVariant {
            return OrderVariant(parcel)
        }

        override fun newArray(size: Int): Array<OrderVariant?> {
            return arrayOfNulls(size)
        }
    }
}