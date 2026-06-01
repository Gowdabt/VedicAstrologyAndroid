package com.astrologyvedic.app.data.repository

import com.astrologyvedic.app.data.local.dao.ProfileDao
import com.astrologyvedic.app.data.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

class ProfileRepository(
    private val profileDao: ProfileDao
) {

    fun getAllProfiles(): Flow<List<ProfileEntity>> =
        profileDao.getAll()

    suspend fun getDefaultProfile(): ProfileEntity? =
        profileDao.getDefault()

    suspend fun getProfileById(id: Long): ProfileEntity? =
        profileDao.getById(id)

    suspend fun insertProfile(profile: ProfileEntity): Result<Long> {
        return try {
            if (profile.isDefault) {
                profileDao.clearDefaultFlag()
            }
            val id = profileDao.insert(profile)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(profile: ProfileEntity): Result<Unit> {
        return try {
            if (profile.isDefault) {
                profileDao.clearDefaultFlag()
            }
            profileDao.update(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProfile(profile: ProfileEntity): Result<Unit> {
        return try {
            profileDao.delete(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setDefault(profileId: Long): Result<Unit> {
        return try {
            val profile = profileDao.getById(profileId)
                ?: return Result.failure(Exception("Profile not found"))
            profileDao.clearDefaultFlag()
            profileDao.update(profile.copy(isDefault = true))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
