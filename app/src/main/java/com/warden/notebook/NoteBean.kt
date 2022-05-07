package com.warden.notebook

import android.os.Parcel
import android.os.Parcelable

class NoteBean() : Parcelable{
    var content: String = ""
    var createTime: Long = System.currentTimeMillis()
    var editTime: Long = System.currentTimeMillis()

    constructor(parcel: Parcel) : this() {
        content = parcel.readString().toString()
        createTime = parcel.readLong()
        editTime = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeLong(createTime)
        parcel.writeLong(editTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteBean> {
        override fun createFromParcel(parcel: Parcel): NoteBean {
            return NoteBean(parcel)
        }

        override fun newArray(size: Int): Array<NoteBean?> {
            return arrayOfNulls(size)
        }
    }
}