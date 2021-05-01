package com.example.flowwebsocket.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flowwebsocket.data.source.entity.RankingEntity

@Database(entities = [RankingEntity::class], version = 1, exportSchema = false)
abstract class RoomDatabaseClient: RoomDatabase() {

    abstract fun roomDao(): RankingDao

    companion object {
        private const val TABLE_NAME = "room_flow"

        fun setupDatabase(context: Context) = Room.databaseBuilder(
            context,
            RoomDatabaseClient::class.java,
            TABLE_NAME
        ).build()
    }

}