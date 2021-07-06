package com.webgurus.attendanceportal.pojo

import android.os.Parcel
import android.os.Parcelable

data class PermissionListPojo(
    val `data`: List<AllListPermissionData>,
    val message: String,
    val status: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<AllListPermissionData>().apply {
            parcel.readList(this as ArrayList<*>,
                GetCustomerLisiting::class.java.classLoader)
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

    companion object CREATOR : Parcelable.Creator<PermissionListPojo> {
        override fun createFromParcel(parcel: Parcel): PermissionListPojo {
            return PermissionListPojo(parcel)
        }

        override fun newArray(size: Int): Array<PermissionListPojo?> {
            return arrayOfNulls(size)
        }
    }
}

data class AllListPermissionData(
    val id: Int,
    val permission: String,
    val role: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(permission)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllListPermissionData> {
        override fun createFromParcel(parcel: Parcel): AllListPermissionData {
            return AllListPermissionData(parcel)
        }

        override fun newArray(size: Int): Array<AllListPermissionData?> {
            return arrayOfNulls(size)
        }
    }
}