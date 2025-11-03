package com.apps.footbalpredictor.hsgrjs.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.footbalpredictor.hsgrjs.data.models.*
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MatchSimulationViewModel(private val repository: FootballRepository) : ViewModel() {
    
    private val _currentMatch = MutableLiveData<Match>()
    val currentMatch: LiveData<Match> = _currentMatch
    
    private val _currentMinute = MutableLiveData(0)
    val currentMinute: LiveData<Int> = _currentMinute
    
    private val _currentHalf = MutableLiveData(1)
    val currentHalf: LiveData<Int> = _currentHalf
    
    private val _matchStatistics = MutableLiveData<MatchStatistics>()
    val matchStatistics: LiveData<MatchStatistics> = _matchStatistics
    
    private val _newEvent = MutableLiveData<MatchEvent?>()
    val newEvent: LiveData<MatchEvent?> = _newEvent
    
    private val _halfTimeReached = MutableLiveData(false)
    val halfTimeReached: LiveData<Boolean> = _halfTimeReached
    
    private val _matchFinished = MutableLiveData(false)
    val matchFinished: LiveData<Boolean> = _matchFinished
    
    private var simulationJob: Job? = null
    
    fun startMatch(homeTeam: Team, awayTeam: Team, leagueId: String) {
        val match = Match(
            id = "${System.currentTimeMillis()}",
            leagueId = leagueId,
            homeTeam = homeTeam,
            awayTeam = awayTeam
        )
        _currentMatch.value = match
        _currentMinute.value = 0
        _currentHalf.value = 1
        _halfTimeReached.value = false
        _matchFinished.value = false
        
        startSimulation()
    }
    
    fun continueSecondHalf() {
        _currentHalf.value = 2
        _halfTimeReached.value = false
        _currentMinute.value = 45
        startSimulation()
    }
    
    private fun startSimulation() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            val startMinute = _currentMinute.value ?: 0
            val endMinute = if (_currentHalf.value == 1) 45 else 90
            
            val msPerMinute = 333L
            
            for (minute in startMinute until endMinute) {
                delay(msPerMinute)
                _currentMinute.value = minute + 1
                
                simulateMinute(minute + 1)
            }
            
            if (_currentHalf.value == 1) {
                val match = _currentMatch.value ?: return@launch
                _currentMatch.value = match.copy(
                    halfTimeHomeScore = match.homeScore,
                    halfTimeAwayScore = match.awayScore
                )
                generateHalfTimeStatistics()
                _halfTimeReached.value = true
            } else {
                generateFullTimeStatistics()
                _matchFinished.value = true
                saveMatchResult()
            }
        }
    }
    
    private fun simulateMinute(minute: Int) {
        val match = _currentMatch.value ?: return
        
        val random = Random.nextFloat()
        
        when {
            random < 0.03f -> { // 3% goal chance
                val isHomeTeam = Random.nextBoolean()
                val teamId = if (isHomeTeam) match.homeTeam.id else match.awayTeam.id
                
                val event = MatchEvent(
                    minute = minute,
                    teamId = teamId,
                    eventType = EventType.GOAL
                )
                
                addEvent(event)
                
                if (isHomeTeam) {
                    _currentMatch.value = match.copy(
                        homeScore = match.homeScore + 1,
                        events = match.events + event
                    )
                } else {
                    _currentMatch.value = match.copy(
                        awayScore = match.awayScore + 1,
                        events = match.events + event
                    )
                }
            }
            random < 0.06f -> { // 3% yellow card chance
                val isHomeTeam = Random.nextBoolean()
                val teamId = if (isHomeTeam) match.homeTeam.id else match.awayTeam.id
                
                val event = MatchEvent(
                    minute = minute,
                    teamId = teamId,
                    eventType = EventType.YELLOW_CARD
                )
                
                addEvent(event)
                _currentMatch.value = match.copy(events = match.events + event)
            }
            random < 0.07f -> { // 1% red card chance
                val isHomeTeam = Random.nextBoolean()
                val teamId = if (isHomeTeam) match.homeTeam.id else match.awayTeam.id
                
                val event = MatchEvent(
                    minute = minute,
                    teamId = teamId,
                    eventType = EventType.RED_CARD
                )
                
                addEvent(event)
                _currentMatch.value = match.copy(events = match.events + event)
            }
        }
    }
    
    private fun addEvent(event: MatchEvent) {
        _newEvent.value = event
        viewModelScope.launch {
            delay(100)
            _newEvent.value = null
        }
    }
    
    private fun generateHalfTimeStatistics() {
        val match = _currentMatch.value ?: return
        
        val scoreDiff = match.homeScore - match.awayScore
        val basePossession = 50
        val possessionDiff = scoreDiff * 5 + Random.nextInt(-10, 11)
        
        val homePossession = (basePossession + possessionDiff).coerceIn(35, 65)
        val awayPossession = 100 - homePossession
        
        val stats = MatchStatistics(
            possession = Pair(homePossession, awayPossession),
            shots = Pair(
                Random.nextInt(3, 10) + match.homeScore * 2,
                Random.nextInt(3, 10) + match.awayScore * 2
            ),
            shotsOnTarget = Pair(
                Random.nextInt(1, 5) + match.homeScore,
                Random.nextInt(1, 5) + match.awayScore
            ),
            corners = Pair(Random.nextInt(2, 6), Random.nextInt(2, 6)),
            fouls = Pair(Random.nextInt(3, 8), Random.nextInt(3, 8)),
            yellowCards = Pair(
                match.events.count { it.eventType == EventType.YELLOW_CARD && it.teamId == match.homeTeam.id },
                match.events.count { it.eventType == EventType.YELLOW_CARD && it.teamId == match.awayTeam.id }
            ),
            redCards = Pair(
                match.events.count { it.eventType == EventType.RED_CARD && it.teamId == match.homeTeam.id },
                match.events.count { it.eventType == EventType.RED_CARD && it.teamId == match.awayTeam.id }
            ),
            offsides = Pair(Random.nextInt(0, 4), Random.nextInt(0, 4))
        )
        
        _matchStatistics.value = stats
    }
    
    private fun generateFullTimeStatistics() {
        val match = _currentMatch.value ?: return
        val halfTimeStats = _matchStatistics.value
        
        val scoreDiff = match.homeScore - match.awayScore
        val basePossession = 50
        val possessionDiff = scoreDiff * 5 + Random.nextInt(-10, 11)
        
        val homePossession = (basePossession + possessionDiff).coerceIn(35, 65)
        val awayPossession = 100 - homePossession
        
        val stats = MatchStatistics(
            possession = Pair(homePossession, awayPossession),
            shots = Pair(
                Random.nextInt(6, 20) + match.homeScore * 2,
                Random.nextInt(6, 20) + match.awayScore * 2
            ),
            shotsOnTarget = Pair(
                Random.nextInt(2, 10) + match.homeScore,
                Random.nextInt(2, 10) + match.awayScore
            ),
            corners = Pair(Random.nextInt(4, 12), Random.nextInt(4, 12)),
            fouls = Pair(Random.nextInt(6, 16), Random.nextInt(6, 16)),
            yellowCards = Pair(
                match.events.count { it.eventType == EventType.YELLOW_CARD && it.teamId == match.homeTeam.id },
                match.events.count { it.eventType == EventType.YELLOW_CARD && it.teamId == match.awayTeam.id }
            ),
            redCards = Pair(
                match.events.count { it.eventType == EventType.RED_CARD && it.teamId == match.homeTeam.id },
                match.events.count { it.eventType == EventType.RED_CARD && it.teamId == match.awayTeam.id }
            ),
            offsides = Pair(Random.nextInt(0, 8), Random.nextInt(0, 8))
        )
        
        _matchStatistics.value = stats
    }
    
    private fun saveMatchResult() {
        val match = _currentMatch.value ?: return
        
        val result = SavedMatchResult(
            leagueId = match.leagueId,
            homeTeamId = match.homeTeam.id,
            awayTeamId = match.awayTeam.id,
            homeScore = match.homeScore,
            awayScore = match.awayScore
        )
        
        repository.saveMatchResult(result)
    }
    
    fun resetMatch() {
        simulationJob?.cancel()
        _currentMatch.value = null
        _currentMinute.value = 0
        _currentHalf.value = 1
        _matchStatistics.value = null
        _newEvent.value = null
        _halfTimeReached.value = false
        _matchFinished.value = false
    }
    
    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}


