package com.mdxam.cars_expenses.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CarLogDb::class],
    version = 1,
    exportSchema = false
)
abstract class CarLogDataBase: RoomDatabase(){

    abstract fun carLogDao() : CarLogDao

    companion object{

        private var instance : CarLogDataBase ?= null

        private val LOCK = Any()

        fun getInstance(context: Context): CarLogDataBase{
            instance?.let {
                return it
            }
            synchronized(LOCK){
                instance?.let {
                    return it
                }
            }
            return Room.databaseBuilder(context = context,
                klass = CarLogDataBase::class.java,
                name = "dataBaseCar"
                ).build().also { instance = it }
        }
    }
}