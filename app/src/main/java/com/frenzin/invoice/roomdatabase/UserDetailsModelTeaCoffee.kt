package com.frenzin.invoice.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customerdetailstea")
class UserDetailsModelTeaCoffee {
    @PrimaryKey(autoGenerate = true)
    var key = 0

    @ColumnInfo(name = "userid")
    var userId: String? = null

    @ColumnInfo(name = "fname")
    var fname: String? = null

    @ColumnInfo(name = "createddate")
    var createdDate: String? = null

    @ColumnInfo(name = "teaitemmorning")
    var teaItemMrng: String? = null

    @ColumnInfo(name = "coffeeitemmorning")
    var coffeeItemMrng: String? = null

    @ColumnInfo(name = "teaitemevening")
    var teaItemEvng: String? = null

    @ColumnInfo(name = "coffeeitemevening")
    var coffeeItemEvng: String? = null


}