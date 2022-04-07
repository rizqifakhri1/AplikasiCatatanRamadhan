package com.binar.aplikasicatatanramadhan

import androidx.room.*

interface RamadhanDao {
    @Query("SELECT * FROM RamadhanEntity")
    fun getAllData(): List<RamadhanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRamadhan (ramadhanEntity: RamadhanEntity):Long

    @Update
    fun updateRamadhan (ramadhanEntity: RamadhanEntity):Int

    @Delete
    fun deleteRamadhan (ramadhanEntity: RamadhanEntity):Int
}







//Catatan
//Sebelum titik dua nama variabel : tipe data
//fun insertRamadhan (ramadhanEntity: RamadhanEntity)

//Insert Update Delete udah ketentuan