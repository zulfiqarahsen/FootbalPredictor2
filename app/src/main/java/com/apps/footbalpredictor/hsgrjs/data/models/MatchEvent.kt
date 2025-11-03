package com.apps.footbalpredictor.hsgrjs.data.models

enum class EventType {
    GOAL,
    YELLOW_CARD,
    RED_CARD
}

data class MatchEvent(
    val minute: Int,
    val teamId: String,
    val eventType: EventType,
    val playerName: String = ""
)


