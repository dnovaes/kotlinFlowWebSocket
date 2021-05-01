package com.example.flowwebsocket.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ranking")
data class RankingEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val count: Int
)