package com.apps.footbalpredictor.hsgrjs.data.models

data class MatchStatistics(
    val possession: Pair<Int, Int>,
    val shots: Pair<Int, Int>,
    val shotsOnTarget: Pair<Int, Int>,
    val corners: Pair<Int, Int>,
    val fouls: Pair<Int, Int>,
    val yellowCards: Pair<Int, Int>,
    val redCards: Pair<Int, Int>,
    val offsides: Pair<Int, Int>
)
