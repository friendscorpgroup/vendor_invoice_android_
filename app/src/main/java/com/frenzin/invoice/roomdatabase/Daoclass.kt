package com.frenzin.invoice.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface Daoclass {

    @Insert
    fun insertAllData(model: UserModel?)

    @Insert
    fun insertOwnerData(model: UserModelOwnerData?)

    @Insert
    fun insertDetailsTea(model: UserDetailsModelTeaCoffee?)

    @Insert
    fun insertDetailsWater(model: UserDetailsModelWater?)

    @Query("select * from  customerdata")
    fun getAllData(): List<UserModel>?

    @Query("select * from  ownerdata where customertype=:customertype")
    fun getOwnerData(customertype: String?): List<UserModelOwnerData>

    @Query("select * from customerdata where morningtime between :starttime and :endtime")
    fun getCustomerMorningTimeList(starttime: String?, endtime: String?): List<UserModel>?

    @Query("select * from customerdata where eveningtime between :starttime and :endtime")
    fun getCustomerEveningTimeList(starttime: String?, endtime: String?): List<UserModel>?

    @Query("select * from customerdata where `key`=:id")
    fun getCustomer(id: Int): List<UserModel>?

    @Query("select * from  customerdetailstea")
    fun getAllCustomerDetails(): List<UserDetailsModelTeaCoffee>?

    @Query("select * from  customerdetailswater")
    fun getAllCustomerDetailsWater(): List<UserDetailsModelWater>?

    @Query("select * from customerdata where customertype=:customertype")
    fun getCustomerDataFromType(customertype: String): List<UserModel>?

    @Query("select * from customerdetailstea where createddate=:date AND userid=:id")
    fun getTeaCoffeeCountData(date: String, id: Int): List<UserDetailsModelTeaCoffee>?

    @Query("select * from customerdetailswater where createddate=:date AND userid=:id")
    fun getWaterBottleCountData(date: String, id: Int): List<UserDetailsModelWater>?

    @Query("select * from customerdetailstea where userid=:id")
    fun getCustomerData(id: Int): List<UserDetailsModelTeaCoffee>?

    @Query("select * from customerdetailstea where createddate=:created AND userid=:id")
    fun getCustomerDataCreatedDate(created: String,id: Int): List<UserDetailsModelTeaCoffee>?

    @Query("select * from customerdetailswater where createddate=:created AND userid=:id")
    fun getCustomerDataWaterCreatedDate(created: String,id: Int): List<UserDetailsModelWater>?

    @Query("select * from customerdetailswater where userid=:id")
    fun getCustomerDataWater(id: Int): List<UserDetailsModelWater>?

    @Query("DELETE FROM customerdata WHERE `key` = :id")
    fun deleteData(id: Int)

    @Query("UPDATE customerdetailstea SET teaitemmorning =:teaitemmorning, coffeeitemmorning =:coffeeitemmorning, teaitemevening =:teaitemevening, coffeeitemevening=:coffeeitemevening where `key`= :key")
    fun updateData(teaitemmorning: String?, coffeeitemmorning: String?,teaitemevening: String?, coffeeitemevening: String?, key: Int)

    @Query("UPDATE customerdetailswater SET addbottle =:addbottle, emptybottle =:emptybottle where `key`= :key")
    fun updateDataWater(addbottle: String?, emptybottle: String?, key: Int)

    @Query("UPDATE customerdata SET fname = :fname, lname = :lname, officename = :officename, phonenumber = :phonenumber, address = :address, morningtime = :morningTime, eveningtime = :eveningTime ,teaprice = :teaprice, coffeprice = :coffeprice, waterprice = :waterprice, type = :type where `key`= :key")
    fun updateCustomerData(fname: String?, lname: String?, officename: String?, phonenumber: String?, address: String?, morningTime: String?, eveningTime: String?, teaprice: String?, coffeprice: String?,waterprice: String?, type: String?, key: Int) : Int

    @Query("SELECT * FROM customerdetailswater WHERE `userId`= :userId AND createddate BETWEEN :dateStart AND :dateEnd")
    fun getFilteredWaterData(dateStart: String?, dateEnd: String?, userId: Int?) : List<UserDetailsModelWater>?

    @Query("SELECT * FROM customerdetailstea WHERE userId= :userId AND createddate BETWEEN :dateStart AND :dateEnd")
    fun getFilteredTeaData(dateStart: String?, dateEnd: String?, userId: Int?) : List<UserDetailsModelTeaCoffee>?

    @Query("select * from customerdata where customertype=:customertype order by morningtime")
    fun getCustomerDataAccMRNGtime(customertype: String): List<UserModel>?

    @Query("select * from customerdata where customertype=:customertype order by eveningtime")
    fun getCustomerDataAccEVNGtime(customertype: String): List<UserModel>?

    @Query("UPDATE ownerdata SET ownername =:ownername, shopname =:shopname, phonenumber = :phonenumber, address = :address, gstNumber = :gstNumber, image = :image where customertype= :customertype")
    fun updateOwnerDataTea(ownername: String?, shopname: String?, phonenumber:String?, address: String?, gstNumber: String?, image: String?, customertype: String?)

    @Query("UPDATE ownerdata SET ownername =:ownername, shopname =:shopname, phonenumber = :phonenumber, address = :address, gstNumber = :gstNumber, image = :image where customertype= :customertype")
    fun updateOwnerDataWater(ownername: String?, shopname: String?, phonenumber:String?, address: String?, gstNumber: String?, image: String?, customertype: String?)

}
