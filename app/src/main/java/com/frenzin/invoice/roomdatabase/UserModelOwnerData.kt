package com.frenzin.invoice.roomdatabase

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ownerdata")
class UserModelOwnerData {
    @PrimaryKey(autoGenerate = true)
    var key = 0

    @ColumnInfo(name = "createddate")
    var createdDate: String? = null

    @ColumnInfo(name = "customertype")
    var customerType: String? = null

    @ColumnInfo(name = "ownername")
    var ownerName: String? = null

    @ColumnInfo(name = "shopname")
    var shopName: String? = null

    @ColumnInfo(name = "phonenumber")
    var phonenumber: String? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "gstNumber")
    var gstNumber: String? = null

    @ColumnInfo(name = "image")
    var qrCodeImage: String? = null

}

