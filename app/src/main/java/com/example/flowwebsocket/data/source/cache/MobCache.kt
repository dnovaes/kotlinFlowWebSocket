package com.example.flowwebsocket.data.source.cache

interface MobCache {
    var mobSet: HashSet<Int>

    fun hasMobIn(absolutePosition: Int): Boolean
    fun markMob(absolutePosition: Int)
    fun unmarkMob(absolutePosition: Int)
    fun clearMobSet()
}