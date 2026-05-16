package com.example.bbltripplanner.screens.userTrip.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripPhotoDao {
    @Query("SELECT * FROM trip_photos WHERE tripId = :tripId")
    fun getPhotosForTrip(tripId: String): Flow<List<TripPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: TripPhotoEntity): Long

    @Update
    suspend fun updatePhoto(photo: TripPhotoEntity)

    @Query("DELETE FROM trip_photos WHERE id = :id")
    suspend fun deletePhoto(id: Long)
}
