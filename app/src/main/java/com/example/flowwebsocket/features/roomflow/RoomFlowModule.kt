package com.example.flowwebsocket.features.roomflow

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomFlowModule = module {
    viewModel {
        MainViewModel(
            localDataSource = get(),
            socket = get()
        )
    }
}