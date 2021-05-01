package com.example.flowwebsocket.data.source.cache

import com.example.flowwebsocket.socket.log

class MobCacheImpl: MobCache {
    override var mobSet: HashSet<Int> = hashSetOf()

    override fun hasMobIn(absolutePosition: Int): Boolean {
        return mobSet.contains(absolutePosition)
    }

    override fun markMob(absolutePosition: Int) {
        mobSet.add(absolutePosition)
        log("mobs: $mobSet")
    }

    override fun unmarkMob(absolutePosition: Int) {
        mobSet.remove(absolutePosition)
        log("mobs: $mobSet")
    }

    override fun clearMobSet() {
        mobSet.clear()
    }
}