package com.example.bbltripplanner.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bbltripplanner.screens.userTrip.dao.TripPhotoDao
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity

@Database(entities = [TripPhotoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripPhotoDao(): TripPhotoDao
}
