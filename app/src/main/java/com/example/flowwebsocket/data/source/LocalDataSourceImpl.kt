package com.example.flowwebsocket.data.source

import com.example.flowwebsocket.data.source.cache.MobCache
import com.example.flowwebsocket.data.source.entity.RankingEntity
import java.util.UUID

class LocalDataSourceImpl(
    private val rankingDao: RankingDao,
    private val cache: MobCache
) : LocalDataSource {
    override fun hasMobIn(posX: Int, posY: Int): Boolean {
        return cache.hasMobIn(4*posX + posY)
    }

    override fun markMob(posX: Int, posY: Int, valueToBeMarked: Boolean) {
        val absolutePosition = 4*posX + posY
        if (valueToBeMarked)
            cache.markMob(absolutePosition)
        else
            cache.unmarkMob(absolutePosition)
    }

    override fun mobPercentage(): Double {
        return cache.percentageMob()
    }

    override fun saveRanking(username: String, total: Int) {
        val newRanking = RankingEntity(
            id = UUID.randomUUID().toString(),
            name = username,
            count = total
        )
        rankingDao.insert(newRanking)
    }
}
