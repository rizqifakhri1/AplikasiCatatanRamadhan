package com.binar.aplikasicatatanramadhan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RamadhanEntity::class, UserEntity::class], version = 1)
abstract class RamadhanDatabase: RoomDatabase() {
    abstract fun ramadhanDao(): RamadhanDao
    abstract fun userDao(): UserDao

    companion object{
        private var INSTANCE: RamadhanDatabase? = null

        fun getInstance(context: Context): RamadhanDatabase? {
            if(INSTANCE == null){
                synchronized(RamadhanDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    RamadhanDatabase::class.java,"Ramadhan.db").build()
                }
            }
            return  INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}

// Untuk Mengenerate database
// Database -> Gedung
// Entity -> Orangnya
// Dao -> Perilaku
// Entity hanya berupa table