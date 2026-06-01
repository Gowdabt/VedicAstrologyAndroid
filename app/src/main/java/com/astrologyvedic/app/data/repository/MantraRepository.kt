package com.astrologyvedic.app.data.repository

import com.astrologyvedic.app.data.local.dao.MantraCountDao
import com.astrologyvedic.app.data.local.entities.MantraCountEntity
import kotlinx.coroutines.flow.Flow

class MantraRepository(
    private val mantraCountDao: MantraCountDao
) {

    fun getCountsByDate(date: String): Flow<List<MantraCountEntity>> =
        mantraCountDao.getByDate(date)

    suspend fun getTotalCount(mantra: String): Int =
        mantraCountDao.getTotalCount(mantra) ?: 0

    suspend fun incrementCount(mantra: String, date: String, target: Int = 108): Result<MantraCountEntity> {
        return try {
            val existing = mantraCountDao.getByMantraAndDate(mantra, date)
            if (existing != null) {
                val updated = existing.copy(count = existing.count + 1)
                mantraCountDao.update(updated)
                Result.success(updated)
            } else {
                val newEntry = MantraCountEntity(
                    mantra = mantra,
                    count = 1,
                    target = target,
                    date = date
                )
                val id = mantraCountDao.insert(newEntry)
                Result.success(newEntry.copy(id = id))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetCount(mantra: String, date: String): Result<Unit> {
        return try {
            val existing = mantraCountDao.getByMantraAndDate(mantra, date)
            if (existing != null) {
                mantraCountDao.update(existing.copy(count = 0))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTarget(mantra: String, date: String, target: Int): Result<Unit> {
        return try {
            val existing = mantraCountDao.getByMantraAndDate(mantra, date)
            if (existing != null) {
                mantraCountDao.update(existing.copy(target = target))
            } else {
                mantraCountDao.insert(
                    MantraCountEntity(
                        mantra = mantra,
                        count = 0,
                        target = target,
                        date = date
                    )
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
