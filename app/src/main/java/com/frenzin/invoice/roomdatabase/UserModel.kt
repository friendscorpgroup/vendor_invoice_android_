package com.frenzin.invoice.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "customerdata")
class UserModel {
    @PrimaryKey(autoGenerate = true)
    var key = 0

    @ColumnInfo(name = "createddate")
    var createdDate: String? = null

    @ColumnInfo(name = "customertype")
    var customerType: String? = null

    @ColumnInfo(name = "fname")
    var fname: String? = null

    @ColumnInfo(name = "lname")
    var lname: String? = null

    @ColumnInfo(name = "officename")
    var officename: String? = null

    @ColumnInfo(name = "phonenumber")
    var phonenumber: String? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "morningtime")
    var morningtime: String? = null

    @ColumnInfo(name = "eveningtime")
    var eveningtime: String? = null

    @ColumnInfo(name = "teaprice")
    var teaprice: String? = null

    @ColumnInfo(name = "coffeprice")
    var coffeprice: String? = null

    @ColumnInfo(name = "waterprice")
    var waterprice: String? = null
}

