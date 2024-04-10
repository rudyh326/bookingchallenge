package com.example.bookingchallenge.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("select * from databasebooking")
    fun getBookings(): Flow<List<DatabaseBooking>>

    @Insert
    suspend fun insertBooking(booking: DatabaseBooking)
}

@Database(entities = [DatabaseBooking::class], version = 1)
abstract class BookingDatabase : RoomDatabase() {
    abstract val bookingDao: BookingDao
}

private lateinit var INSTANCE: BookingDatabase

fun getDatabase(context: Context): BookingDatabase {
    synchronized(BookingDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                BookingDatabase::class.java,
                "bookings")
                .fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}
