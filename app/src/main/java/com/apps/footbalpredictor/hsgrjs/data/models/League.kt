package com.apps.footbalpredictor.hsgrjs.data.models

data class League(
    val id: String,
    val name: String,
    val country: String,
    val teams: List<Team>
)


