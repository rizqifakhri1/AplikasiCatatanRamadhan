package com.binar.aplikasicatatanramadhan

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize //Optional -> Kalo buat database optional
data class RamadhanEntity (
         @PrimaryKey(autoGenerate = true) var id : Int = 0,
         @ColumnInfo(name = "berpuasa") var berpuasa: Boolean,
         @ColumnInfo(name = "hari") var hari: Int,
         @ColumnInfo(name = "tanggal") var tanggal: String,
         @ColumnInfo(name = "catatan") var catatan: String
        ) : Parcelable