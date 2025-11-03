package com.apps.footbalpredictor.hsgrjs.data.models

data class SavedMatchResult(
    val leagueId: String,
    val homeTeamId: String,
    val awayTeamId: String,
    val homeScore: Int,
    val awayScore: Int,
    val timestamp: Long = System.currentTimeMillis()
)


