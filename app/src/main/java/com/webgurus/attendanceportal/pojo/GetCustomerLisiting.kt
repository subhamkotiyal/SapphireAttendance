package com.webgurus.attendanceportal.pojo

import android.os.Parcel
import android.os.Parcelable

data class GetCustomerLisiting(
    val data: ArrayList<Datar>,
    val message: String,
    val status: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<Datar>().apply {
            parcel.readList(this as ArrayList<*>,
                GetCustomerLisiting::class.java.classLoader)
        },
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        arrayListOf<Datar>().apply {
            parcel.writeList(
                data as ArrayList<*>?
            )
        }
        parcel.writeString(message)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetCustomerLisiting> {
        override fun createFromParcel(parcel: Parcel): GetCustomerLisiting {
            return GetCustomerLisiting(parcel)
        }

        override fun newArray(size: Int): Array<GetCustomerLisiting?> {
            return arrayOfNulls(size)
        }
    }
}

data class Datar(
    val assign_to: Int,
    val assign_user_name: String,
    val city: String,
    val created_at: String,
    val dob: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val middle_name: String,
    val last_order_date: String,
    val last_order_pending: String,
    val orders: ArrayList<Order>,
    val payment_pending: ArrayList<PaymentPending>,
    val phone_number: String,
    val pincode: Int,
    val state: String,
    val address: String,
    val secondary_address: String,
    val isSelected:Int

) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
//        parcel.readString()!!,
        arrayListOf<Order>().apply {
            parcel.readList(this as ArrayList<*>,
                GetCustomerLisiting::class.java.classLoader)
        },
        arrayListOf<PaymentPending>().apply {
            parcel.readList(this as ArrayList<*>,
                GetCustomerLisiting::class.java.classLoader)
        },
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(assign_to)
        parcel.writeString(assign_user_name)
        parcel.writeString(city)
        parcel.writeString(created_at)
        parcel.writeString(dob)
        parcel.writeString(email)
        parcel.writeString(first_name)
        parcel.writeInt(id)
        parcel.writeString(last_name)
        parcel.writeString(middle_name)
        parcel.writeString(last_order_date)
        parcel.writeString(last_order_pending)
      //  parcel.writeString(middle_name)

        arrayListOf<Order>().apply {
            parcel.writeList(
                orders as ArrayList<*>?
            )
        }
        arrayListOf<PaymentPending>().apply {
            parcel.writeList(
                payment_pending as ArrayList<*>?
            )
        }
        parcel.writeString(phone_number)

        parcel.writeInt(pincode)
        parcel.writeString(state)
        parcel.writeString(address)
        parcel.writeString(secondary_address)
        parcel.writeInt(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Datar> {
        override fun createFromParcel(parcel: Parcel): Datar {
            return Datar(parcel)
        }

        override fun newArray(size: Int): Array<Datar?> {
            return arrayOfNulls(size)
        }
    }
}

data class Order(
    val address: String,
    val created_by_id: Int,
    val customer_name: String,
    val date: String,
    val id: Int,
    //val instructions: String,
    val is_payment_complete: Int,
    val payment_pending: Int,
    val payment_received: Int,
    val payment_type: Int,
    val status: Int,
    val total_price: String,
    val variant: ArrayList<VariantData>
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
//        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        arrayListOf<VariantData>().apply {
            parcel.readList(this as ArrayList<*>,
                GetCustomerLisiting::class.java.classLoader)
        }
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeInt(created_by_id)
        parcel.writeString(customer_name)
        parcel.writeString(date)
        parcel.writeInt(id)
      //  parcel.writeString(instructions)
        parcel.writeInt(is_payment_complete)
        parcel.writeInt(payment_pending)
        parcel.writeInt(payment_received)
        parcel.writeInt(payment_type)
        parcel.writeInt(status)
        parcel.writeString(total_price)
        arrayListOf<VariantData>().apply {
            parcel.writeList(
                variant as ArrayList<*>?
            )
        }

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}

data class PaymentPending(
    val date: String,
    val order_id: Int,
    val payment_pending: Int,
    val payment_received: Int,
    val total_payment: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeInt(order_id)
        parcel.writeInt(payment_pending)
        parcel.writeInt(payment_received)
        parcel.writeString(total_payment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentPending> {
        override fun createFromParcel(parcel: Parcel): PaymentPending {
            return PaymentPending(parcel)
        }

        override fun newArray(size: Int): Array<PaymentPending?> {
            return arrayOfNulls(size)
        }
    }
}

data class VariantData(
    val id: Int,
    val max_price: String,
    val min_price: String,
    val product_name: String,
    val product_price: String,
    val product_unit: String,
    val quantity: Int,
    val quoted_price: String,
    val total_amount: String
):Parcelable {
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

    companion object CREATOR : Parcelable.Creator<VariantData> {
        override fun createFromParcel(parcel: Parcel): VariantData {
            return VariantData(parcel)
        }

        override fun newArray(size: Int): Array<VariantData?> {
            return arrayOfNulls(size)
        }
    }
}