package com.example.flowwebsocket.data.source

import com.example.flowwebsocket.data.source.entity.RankingEntity
import java.util.UUID

class RoomDataSourceImpl(
    private val roomDao: RankingDao
) : RoomDataSource {
    override fun saveRanking(username: String, total: Int) {
        val newRanking = RankingEntity(
            id = UUID.randomUUID().toString(),
            name = username,
            count = total
        )
        roomDao.insert(newRanking)
    }
}
