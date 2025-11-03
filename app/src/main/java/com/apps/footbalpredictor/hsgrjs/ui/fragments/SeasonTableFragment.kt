package com.apps.footbalpredictor.hsgrjs.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apps.footbalpredictor.hsgrjs.data.models.League
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository
import com.apps.footbalpredictor.hsgrjs.databinding.FragmentSeasonTableBinding
import com.apps.footbalpredictor.hsgrjs.ui.adapters.LeagueTableAdapter

class SeasonTableFragment : Fragment() {
    
    private var _binding: FragmentSeasonTableBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var repository: FootballRepository
    private lateinit var tableAdapter: LeagueTableAdapter
    private var leagues: List<League> = emptyList()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonTableBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        repository = FootballRepository(requireContext())
        leagues = repository.getLeagues()
        
        setupRecyclerView()
        setupLeagueSpinner()
        setupClickListeners()
        
        if (leagues.isNotEmpty()) {
            loadLeagueIcon(leagues[0].id)
        }
    }
    
    private fun setupRecyclerView() {
        tableAdapter = LeagueTableAdapter()
        binding.recyclerViewTable.adapter = tableAdapter
    }
    
    private fun setupLeagueSpinner() {
        val leagueNames = leagues.map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            com.apps.footbalpredictor.hsgrjs.R.layout.spinner_item,
            leagueNames
        )
        adapter.setDropDownViewResource(com.apps.footbalpredictor.hsgrjs.R.layout.spinner_dropdown_item)
        binding.spinnerLeague.adapter = adapter
        
        binding.spinnerLeague.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLeague = leagues[position]
                loadLeagueIcon(selectedLeague.id)
                loadTable(selectedLeague.id)
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
    
    private fun loadTable(leagueId: String) {
        val table = repository.getLeagueTable(leagueId)
        tableAdapter.submitList(table)
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


