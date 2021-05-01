package com.example.flowwebsocket.data.source

import androidx.room.Dao
import androidx.room.Insert
import com.example.flowwebsocket.data.source.entity.RankingEntity

@Dao
interface RankingDao {
    @Insert
    fun insert(ranking: RankingEntity)
}