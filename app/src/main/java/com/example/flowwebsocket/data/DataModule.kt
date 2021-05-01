package com.example.flowwebsocket.data

import com.example.flowwebsocket.data.source.RankingDao
import com.example.flowwebsocket.data.source.RoomDataSource
import com.example.flowwebsocket.data.source.RoomDataSourceImpl
import com.example.flowwebsocket.data.source.RoomDatabaseClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module { }

val databaseModule = module {
    single { RoomDatabaseClient.setupDatabase(context = androidContext()) }

    single { get<RoomDatabaseClient>().roomDao() }
}

val dataSourceModule = module {
    single<RoomDataSource> {
        RoomDataSourceImpl(roomDao = get())
    }
}

val dataModule = networkModule + dataSourceModule + databaseModule