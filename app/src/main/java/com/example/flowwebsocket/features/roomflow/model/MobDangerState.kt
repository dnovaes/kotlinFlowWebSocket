package com.example.flowwebsocket.features.roomflow.model

sealed class MobDangerState(val description: String) {
    object Peacefull: MobDangerState("Everything looks clear and safe")
    object Attention: MobDangerState("There is something strange...")
    object Caution: MobDangerState("Do we need help?")
    object Danger: MobDangerState("Mama mia let me go! HELP")
    object Emergency: MobDangerState("Run to the hills!!!")

    companion object {
        const val THRESHOLD_PEACEFULL = 0.2
        const val THRESHOLD_ATTENTION = 0.4
        const val THRESHOLD_CAUTION = 0.6
        const val THRESHOLD_DANGER = 0.8
    }
}