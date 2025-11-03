package com.apps.footbalpredictor.hsgrjs.data.models

data class Match(
    val id: String,
    val leagueId: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val events: List<MatchEvent> = emptyList(),
    val isFinished: Boolean = false,
    val halfTimeHomeScore: Int = 0,
    val halfTimeAwayScore: Int = 0
)


