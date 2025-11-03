package com.apps.footbalpredictor.hsgrjs.data.models

data class LeagueTableEntry(
    val team: Team,
    val played: Int = 0,
    val won: Int = 0,
    val drawn: Int = 0,
    val lost: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val points: Int = 0
) {
    val goalDifference: Int
        get() = goalsFor - goalsAgainst
}


