package com.webgurus.attendanceportal.pojo

import android.os.Parcel
import android.os.Parcelable

data class OrderDetailsPojo(
    val order_detail: OrderDetail,
    val remarks: List<Remark>,
    val status: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        (parcel.readValue(OrderDetail::class.java.classLoader) as OrderDetail?)!!,
        arrayListOf<Remark>().apply {
            parcel.readList(this as List<*>,
                OrderDetailsPojo::class.java.classLoader)
        },
        parcel.readInt()
    ) {

    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(order_detail)
        arrayListOf<Remark>().apply { parcel.writeList(remarks as List<Remark>?) }
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetailsPojo> {
        override fun createFromParcel(parcel: Parcel): OrderDetailsPojo {
            return OrderDetailsPojo(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetailsPojo?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrderDetail(
    val address: String,
    val created_by_id: Int,
    val customer_name: String,
    val date: String,
    val id: Int,
    val instructions: String,
    val is_payment_complete: Int,
    val payment_pending: Int,
    val payment_received: Int,
    val payment_type: Int,
    val status: Int,
    val total_price: String,
    val transaction: List<OrderTransaction>,
    val variant: List<OrdersVariant>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        arrayListOf<OrderTransaction>().apply {
            parcel.readList(this as List<*>,
                OrderDetailsPojo::class.java.classLoader)
        },
        arrayListOf<OrdersVariant>().apply {
            parcel.readList(this as List<*>,
                OrderDetailsPojo::class.java.classLoader)
        }
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeInt(created_by_id)
        parcel.writeString(customer_name)
        parcel.writeString(date)
        parcel.writeInt(id)
        parcel.writeString(instructions)
        parcel.writeInt(is_payment_complete)
        parcel.writeInt(payment_pending)
        parcel.writeInt(payment_received)
        parcel.writeInt(payment_type)
        parcel.writeInt(status)
        parcel.writeString(total_price)
        parcel.writeValue(transaction)
        parcel.writeValue(variant)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetail> {
        override fun createFromParcel(parcel: Parcel): OrderDetail {
            return OrderDetail(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetail?> {
            return arrayOfNulls(size)
        }
    }
}

data class Remark(
    val created_at: String,
    val created_by_id: Int,
    val id: Int,
    val order_id: Int,
    val remarks: String,
    val status: Int,
    val updated_at: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeInt(created_by_id)
        parcel.writeInt(id)
        parcel.writeInt(order_id)
        parcel.writeString(remarks)
        parcel.writeInt(status)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Remark> {
        override fun createFromParcel(parcel: Parcel): Remark {
            return Remark(parcel)
        }

        override fun newArray(size: Int): Array<Remark?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrderTransaction(
    val amount: String,
    val created_at: String,
    val created_by_id: Int,
    val id: Int,
    val order_id: Int,
    val status: Int,
    val transaction_id: String,
    val updated_at: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(amount)
        parcel.writeString(created_at)
        parcel.writeInt(created_by_id)
        parcel.writeInt(id)
        parcel.writeInt(order_id)
        parcel.writeInt(status)
        parcel.writeString(transaction_id)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderTransaction> {
        override fun createFromParcel(parcel: Parcel): OrderTransaction {
            return OrderTransaction(parcel)
        }

        override fun newArray(size: Int): Array<OrderTransaction?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrdersVariant(
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

    companion object CREATOR : Parcelable.Creator<OrdersVariant> {
        override fun createFromParcel(parcel: Parcel): OrdersVariant {
            return OrdersVariant(parcel)
        }

        override fun newArray(size: Int): Array<OrdersVariant?> {
            return arrayOfNulls(size)
        }
    }
}