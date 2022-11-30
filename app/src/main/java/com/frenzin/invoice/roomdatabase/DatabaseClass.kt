package com.frenzin.invoice.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class, UserDetailsModelTeaCoffee::class, UserDetailsModelWater::class, UserModelOwnerData::class], version = 1)
abstract class DatabaseClass : RoomDatabase() {

    abstract val dao: Daoclass?

    companion object {
        private var instance: DatabaseClass? = null
        fun getDatabase(context: Context?): DatabaseClass? {
            if (instance == null) {
                synchronized(DatabaseClass::class.java) {
                    instance = Room.databaseBuilder(
                        context!!,
                        DatabaseClass::class.java, "invoice"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }

    }
}