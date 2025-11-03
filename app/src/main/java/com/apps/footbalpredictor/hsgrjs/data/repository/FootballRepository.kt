package com.apps.footbalpredictor.hsgrjs.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.apps.footbalpredictor.hsgrjs.data.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FootballRepository(context: Context) {
    
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("football_data", Context.MODE_PRIVATE)
    
    private val gson = Gson()
    
    companion object {
        private const val KEY_MATCH_RESULTS = "match_results"
    }
    
    fun getLeagues(): List<League> {
        return listOf(
            League(
                id = "premier_league",
                name = "Premier League",
                country = "England",
                teams = getPremierLeagueTeams()
            ),
            League(
                id = "la_liga",
                name = "La Liga",
                country = "Spain",
                teams = getLaLigaTeams()
            ),
            League(
                id = "bundesliga",
                name = "Bundesliga",
                country = "Germany",
                teams = getBundesligaTeams()
            ),
            League(
                id = "serie_a",
                name = "Serie A",
                country = "Italy",
                teams = getSerieATeams()
            ),
            League(
                id = "ligue_1",
                name = "Ligue 1",
                country = "France",
                teams = getLigue1Teams()
            )
        )
    }
    
    private fun getPremierLeagueTeams(): List<Team> {
        return listOf(
            Team("pl_1", "Manchester City", "premier_league"),
            Team("pl_2", "Arsenal", "premier_league"),
            Team("pl_3", "Liverpool", "premier_league"),
            Team("pl_4", "Aston Villa", "premier_league"),
            Team("pl_5", "Tottenham", "premier_league"),
            Team("pl_6", "Chelsea", "premier_league"),
            Team("pl_7", "Newcastle", "premier_league"),
            Team("pl_8", "Manchester United", "premier_league"),
            Team("pl_9", "West Ham", "premier_league"),
            Team("pl_10", "Brighton", "premier_league"),
            Team("pl_11", "Bournemouth", "premier_league"),
            Team("pl_12", "Crystal Palace", "premier_league"),
            Team("pl_13", "Fulham", "premier_league"),
            Team("pl_14", "Wolverhampton", "premier_league"),
            Team("pl_15", "Everton", "premier_league"),
            Team("pl_16", "Brentford", "premier_league"),
            Team("pl_17", "Nottingham Forest", "premier_league"),
            Team("pl_18", "Luton Town", "premier_league"),
            Team("pl_19", "Burnley", "premier_league"),
            Team("pl_20", "Sheffield United", "premier_league")
        )
    }
    
    private fun getLaLigaTeams(): List<Team> {
        return listOf(
            Team("ll_1", "Real Madrid", "la_liga"),
            Team("ll_2", "Barcelona", "la_liga"),
            Team("ll_3", "Atletico Madrid", "la_liga"),
            Team("ll_4", "Real Sociedad", "la_liga"),
            Team("ll_5", "Athletic Bilbao", "la_liga"),
            Team("ll_6", "Villarreal", "la_liga"),
            Team("ll_7", "Valencia", "la_liga"),
            Team("ll_8", "Betis", "la_liga"),
            Team("ll_9", "Girona", "la_liga"),
            Team("ll_10", "Sevilla", "la_liga"),
            Team("ll_11", "Osasuna", "la_liga"),
            Team("ll_12", "Getafe", "la_liga"),
            Team("ll_13", "Rayo Vallecano", "la_liga"),
            Team("ll_14", "Cadiz", "la_liga"),
            Team("ll_15", "Mallorca", "la_liga"),
            Team("ll_16", "Alaves", "la_liga"),
            Team("ll_17", "Celta Vigo", "la_liga"),
            Team("ll_18", "Almeria", "la_liga"),
            Team("ll_19", "Granada", "la_liga"),
            Team("ll_20", "Las Palmas", "la_liga")
        )
    }
    
    private fun getBundesligaTeams(): List<Team> {
        return listOf(
            Team("bl_1", "Bayer Leverkusen", "bundesliga"),
            Team("bl_2", "Bayern Munich", "bundesliga"),
            Team("bl_3", "Stuttgart", "bundesliga"),
            Team("bl_4", "RB Leipzig", "bundesliga"),
            Team("bl_5", "Borussia Dortmund", "bundesliga"),
            Team("bl_6", "Eintracht Frankfurt", "bundesliga"),
            Team("bl_7", "Hoffenheim", "bundesliga"),
            Team("bl_8", "Werder Bremen", "bundesliga"),
            Team("bl_9", "Freiburg", "bundesliga"),
            Team("bl_10", "Wolfsburg", "bundesliga"),
            Team("bl_11", "Augsburg", "bundesliga"),
            Team("bl_12", "Mainz", "bundesliga"),
            Team("bl_13", "Borussia Monchengladbach", "bundesliga"),
            Team("bl_14", "Union Berlin", "bundesliga"),
            Team("bl_15", "Bochum", "bundesliga"),
            Team("bl_16", "Cologne", "bundesliga"),
            Team("bl_17", "Heidenheim", "bundesliga"),
            Team("bl_18", "Darmstadt", "bundesliga")
        )
    }
    
    private fun getSerieATeams(): List<Team> {
        return listOf(
            Team("sa_1", "Inter Milan", "serie_a"),
            Team("sa_2", "AC Milan", "serie_a"),
            Team("sa_3", "Juventus", "serie_a"),
            Team("sa_4", "Atalanta", "serie_a"),
            Team("sa_5", "Bologna", "serie_a"),
            Team("sa_6", "Roma", "serie_a"),
            Team("sa_7", "Napoli", "serie_a"),
            Team("sa_8", "Lazio", "serie_a"),
            Team("sa_9", "Fiorentina", "serie_a"),
            Team("sa_10", "Torino", "serie_a"),
            Team("sa_11", "Genoa", "serie_a"),
            Team("sa_12", "Monza", "serie_a"),
            Team("sa_13", "Verona", "serie_a"),
            Team("sa_14", "Cagliari", "serie_a"),
            Team("sa_15", "Udinese", "serie_a"),
            Team("sa_16", "Frosinone", "serie_a"),
            Team("sa_17", "Salernitana", "serie_a"),
            Team("sa_18", "Empoli", "serie_a"),
            Team("sa_19", "Sassuolo", "serie_a"),
            Team("sa_20", "Lecce", "serie_a")
        )
    }
    
    private fun getLigue1Teams(): List<Team> {
        return listOf(
            Team("l1_1", "PSG", "ligue_1"),
            Team("l1_2", "Monaco", "ligue_1"),
            Team("l1_3", "Brest", "ligue_1"),
            Team("l1_4", "Lille", "ligue_1"),
            Team("l1_5", "Nice", "ligue_1"),
            Team("l1_6", "Lens", "ligue_1"),
            Team("l1_7", "Marseille", "ligue_1"),
            Team("l1_8", "Lyon", "ligue_1"),
            Team("l1_9", "Rennes", "ligue_1"),
            Team("l1_10", "Reims", "ligue_1"),
            Team("l1_11", "Montpellier", "ligue_1"),
            Team("l1_12", "Strasbourg", "ligue_1"),
            Team("l1_13", "Nantes", "ligue_1"),
            Team("l1_14", "Toulouse", "ligue_1"),
            Team("l1_15", "Le Havre", "ligue_1"),
            Team("l1_16", "Le Mans", "ligue_1"),
            Team("l1_17", "Metz", "ligue_1"),
            Team("l1_18", "Clermont", "ligue_1")
        )
    }
    
    fun saveMatchResult(result: SavedMatchResult) {
        val results = getMatchResults().toMutableList()
        results.add(result)
        
        val json = gson.toJson(results)
        sharedPreferences.edit()
            .putString(KEY_MATCH_RESULTS, json)
            .apply()
    }
    
    fun getMatchResults(): List<SavedMatchResult> {
        val json = sharedPreferences.getString(KEY_MATCH_RESULTS, null) ?: return emptyList()
        val type = object : TypeToken<List<SavedMatchResult>>() {}.type
        return gson.fromJson(json, type)
    }
    
    fun getMatchResultsForLeague(leagueId: String): List<SavedMatchResult> {
        return getMatchResults().filter { it.leagueId == leagueId }
    }
    
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
    
    fun getLeagueTable(leagueId: String): List<LeagueTableEntry> {
        val league = getLeagues().find { it.id == leagueId } ?: return emptyList()
        val matchResults = getMatchResultsForLeague(leagueId)
        
        val tableMap = league.teams.associate { team ->
            team.id to LeagueTableEntry(team = team)
        }.toMutableMap()
        
        matchResults.forEach { result ->
            val homeEntry = tableMap[result.homeTeamId] ?: return@forEach
            val awayEntry = tableMap[result.awayTeamId] ?: return@forEach
            
            tableMap[result.homeTeamId] = homeEntry.copy(
                played = homeEntry.played + 1,
                won = homeEntry.won + if (result.homeScore > result.awayScore) 1 else 0,
                drawn = homeEntry.drawn + if (result.homeScore == result.awayScore) 1 else 0,
                lost = homeEntry.lost + if (result.homeScore < result.awayScore) 1 else 0,
                goalsFor = homeEntry.goalsFor + result.homeScore,
                goalsAgainst = homeEntry.goalsAgainst + result.awayScore,
                points = homeEntry.points + when {
                    result.homeScore > result.awayScore -> 3
                    result.homeScore == result.awayScore -> 1
                    else -> 0
                }
            )
            
            tableMap[result.awayTeamId] = awayEntry.copy(
                played = awayEntry.played + 1,
                won = awayEntry.won + if (result.awayScore > result.homeScore) 1 else 0,
                drawn = awayEntry.drawn + if (result.homeScore == result.awayScore) 1 else 0,
                lost = awayEntry.lost + if (result.awayScore < result.homeScore) 1 else 0,
                goalsFor = awayEntry.goalsFor + result.awayScore,
                goalsAgainst = awayEntry.goalsAgainst + result.homeScore,
                points = awayEntry.points + when {
                    result.awayScore > result.homeScore -> 3
                    result.homeScore == result.awayScore -> 1
                    else -> 0
                }
            )
        }
        
        return tableMap.values.sortedWith(
            compareByDescending<LeagueTableEntry> { it.points }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
        )
    }
}


