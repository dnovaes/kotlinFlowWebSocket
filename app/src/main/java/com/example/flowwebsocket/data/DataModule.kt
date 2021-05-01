package com.example.flowwebsocket.data

import com.example.flowwebsocket.data.source.LocalDataSource
import com.example.flowwebsocket.data.source.LocalDataSourceImpl
import com.example.flowwebsocket.data.source.RoomDatabaseClient
import com.example.flowwebsocket.data.source.cache.MobCache
import com.example.flowwebsocket.data.source.cache.MobCacheImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module { }

val databaseModule = module {
    single { RoomDatabaseClient.setupDatabase(context = androidContext()) }

    single { get<RoomDatabaseClient>().roomDao() }
}

val cacheModule = module {
    single<MobCache> { MobCacheImpl() }
}

val dataSourceModule = module {
    single<LocalDataSource> {
        LocalDataSourceImpl(cache = get(), rankingDao = get())
    }
}

val dataModule = networkModule + dataSourceModule + databaseModule + cacheModule