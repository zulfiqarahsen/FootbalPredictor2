package com.apps.footbalpredictor.hsgrjs.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apps.footbalpredictor.hsgrjs.R
import com.apps.footbalpredictor.hsgrjs.data.models.League
import com.apps.footbalpredictor.hsgrjs.data.models.Team
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository
import com.apps.footbalpredictor.hsgrjs.databinding.FragmentMatchSetupBinding

class MatchSetupFragment : Fragment() {
    
    private var _binding: FragmentMatchSetupBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var repository: FootballRepository
    private var leagues: List<League> = emptyList()
    private var selectedLeague: League? = null
    private var selectedHomeTeam: Team? = null
    private var selectedAwayTeam: Team? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchSetupBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        repository = FootballRepository(requireContext())
        leagues = repository.getLeagues()
        
        setupLeagueSpinner()
        setupClickListeners()
        
        if (leagues.isNotEmpty()) {
            loadLeagueIcon(leagues[0].id)
        }
    }
    
    private fun setupLeagueSpinner() {
        val leagueNames = leagues.map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            leagueNames
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerLeague.adapter = adapter
        
        binding.spinnerLeague.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLeague = leagues[position]
                loadLeagueIcon(leagues[position].id)
                setupTeamSpinners()
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun loadLeagueIcon(leagueId: String) {
        val resourceName = "league_${leagueId}"
        val resourceId = resources.getIdentifier(resourceName, "drawable", requireContext().packageName)
        if (resourceId != 0) {
            binding.ivLeagueIcon.setImageResource(resourceId)
        }
    }
    
    private fun setupTeamSpinners() {
        val league = selectedLeague ?: return
        val teamNames = league.teams.map { it.name }
        
        val homeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            teamNames
        )
        homeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerHomeTeam.adapter = homeAdapter
        
        binding.spinnerHomeTeam.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedHomeTeam = league.teams[position]
                loadTeamIcon(league.teams[position].id, binding.ivHomeTeamIcon)
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        val awayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            teamNames
        )
        awayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerAwayTeam.adapter = awayAdapter
        
        binding.spinnerAwayTeam.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAwayTeam = league.teams[position]
                loadTeamIcon(league.teams[position].id, binding.ivAwayTeamIcon)
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        if (league.teams.isNotEmpty()) {
            loadTeamIcon(league.teams[0].id, binding.ivHomeTeamIcon)
            loadTeamIcon(league.teams[0].id, binding.ivAwayTeamIcon)
        }
    }
    
    private fun loadTeamIcon(teamId: String, imageView: android.widget.ImageView) {
        val resourceName = "team_${teamId}"
        val resourceId = resources.getIdentifier(resourceName, "drawable", requireContext().packageName)
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnStartSimulation.setOnClickListener {
            val homeTeam = selectedHomeTeam
            val awayTeam = selectedAwayTeam
            val league = selectedLeague
            
            if (homeTeam == null || awayTeam == null || league == null) {
                Toast.makeText(requireContext(), "Select teams", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (homeTeam.id == awayTeam.id) {
                Toast.makeText(requireContext(), "Teams must be different", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val bundle = Bundle().apply {
                putString("homeTeamId", homeTeam.id)
                putString("homeTeamName", homeTeam.name)
                putString("awayTeamId", awayTeam.id)
                putString("awayTeamName", awayTeam.name)
                putString("leagueId", league.id)
            }
            
            findNavController().navigate(R.id.action_matchSetupFragment_to_matchSimulationFragment, bundle)
        }
        
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

