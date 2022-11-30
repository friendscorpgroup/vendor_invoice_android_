package com.frenzin.invoice.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "customerdetailswater")
class UserDetailsModelWater {
    @PrimaryKey(autoGenerate = true)
    var key = 0

    @ColumnInfo(name = "userid")
    var userId: String? = null

    @ColumnInfo(name = "fname")
    var fname: String? = null

    @ColumnInfo(name = "createddate")
    var createdDate: String? = null

    @ColumnInfo(name = "addbottle")
    var addbottle: String? = null

    @ColumnInfo(name = "emptybottle")
    var emptybottle: String? = null

}