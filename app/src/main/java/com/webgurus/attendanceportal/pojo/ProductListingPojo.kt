package com.webgurus.attendanceportal.pojo

import android.os.Parcel
import android.os.Parcelable

data class ProductListingPojo(
    val `data`: List<ProductData>,
    val message: String,
    val status: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<ProductData>().apply {
            parcel.readList(this as List<*>,
                ProductListingPojo::class.java.classLoader)
        },
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        arrayListOf<ProductData>().apply { parcel!!.writeList(data as List<ProductData>?) }
        parcel!!.writeString(message)
        parcel!!.writeInt(status)
    }

    companion object CREATOR : Parcelable.Creator<ProductListingPojo> {
        override fun createFromParcel(parcel: Parcel): ProductListingPojo {
            return ProductListingPojo(parcel)
        }

        override fun newArray(size: Int): Array<ProductListingPojo?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProductData(
    val id: Int,
    val name: String,
    val variants: List<Variant>
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        arrayListOf<Variant>().apply {
            parcel.readList(this as List<*>,
                ProductListingPojo::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        arrayListOf<Variant>().apply { parcel.writeList(variants as List<Variant>?) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductData> {
        override fun createFromParcel(parcel: Parcel): ProductData {
            return ProductData(parcel)
        }

        override fun newArray(size: Int): Array<ProductData?> {
            return arrayOfNulls(size)
        }
    }
}

data class Variant(
    val category_id: Int,
    val created_on: String,
    val category_name: String,
    val unit_name: String,
    val id: Int,
    val in_stock: Int,
    val max_price: String,
    val min_price: String,
    val price: String,
    val product_id: Int,
    val product_name: String,
    val quantity_left: Int,
    val unit_id: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(category_id)
        parcel.writeString(created_on)
        parcel.writeString(category_name)
        parcel.writeString(unit_name)
        parcel.writeInt(id)
        parcel.writeInt(in_stock)
        parcel.writeString(max_price)
        parcel.writeString(min_price)
        parcel.writeString(price)
        parcel.writeInt(product_id)
        parcel.writeString(product_name)
        parcel.writeInt(quantity_left)
        parcel.writeInt(unit_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Variant> {
        override fun createFromParcel(parcel: Parcel): Variant {
            return Variant(parcel)
        }

        override fun newArray(size: Int): Array<Variant?> {
            return arrayOfNulls(size)
        }
    }
}
