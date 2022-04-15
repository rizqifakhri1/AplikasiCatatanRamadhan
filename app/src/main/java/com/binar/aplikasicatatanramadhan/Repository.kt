package com.binar.aplikasicatatanramadhan

import androidx.annotation.WorkerThread

class Repository (private val ramadhanDao: RamadhanDao, private val userDao: UserDao) {
    val ramadhan: List<RamadhanEntity> = ramadhanDao.getAllData()

    @Suppress("RedudantSuspendModifier")
    @WorkerThread
    suspend fun insert(ramadhanEntity: RamadhanEntity): Long {
        return  ramadhanDao.insertRamadhan(ramadhanEntity)
    }

    fun update(ramadhanEntity: RamadhanEntity) : Int {
        return ramadhanDao.updateRamadhan(ramadhanEntity)
    }

    fun delete(ramadhanEntity: RamadhanEntity): Int {
        return ramadhanDao.deleteRamadhan(ramadhanEntity)
    }

    fun insertData (userEntity: UserEntity): Long {
        return userDao.insertUser(userEntity)
    }
}