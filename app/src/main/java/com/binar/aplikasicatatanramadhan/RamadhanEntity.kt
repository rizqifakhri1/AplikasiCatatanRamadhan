package com.binar.aplikasicatatanramadhan

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize //Optional -> Kalo buat database optional
data class RamadhanEntity (
         @PrimaryKey(autoGenerate = true) var id : Int?,
         @ColumnInfo(name = "berpuasa") var berpuasa: Boolean,
         @ColumnInfo(name = "hari") var hari: Int,
         @ColumnInfo(name = "tanggal") var tanggal: String,
         @ColumnInfo(name = "catatan") var catatan: String,
         @ColumnInfo(name = "lima_waktu") var lima_waktu: Boolean,
         @ColumnInfo(name = "tarawih") var tarawih: Boolean,
         @ColumnInfo(name = "tahajud") var tahajud: Boolean,
         @ColumnInfo(name = "quran") var quran: Boolean,
         @ColumnInfo(name = "sedekah") var sedekah: Boolean,
         @ColumnInfo(name = "kajian") var kajian: Boolean,
        ) : Parcelable