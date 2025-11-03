package com.apps.footbalpredictor.hsgrjs.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.apps.footbalpredictor.hsgrjs.R
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository
import com.apps.footbalpredictor.hsgrjs.databinding.FragmentHalfTimeStatsBinding
import com.apps.footbalpredictor.hsgrjs.ui.viewmodel.MatchSimulationViewModel
import com.apps.footbalpredictor.hsgrjs.ui.viewmodel.ViewModelFactory

class HalfTimeStatsFragment : Fragment() {
    
    private var _binding: FragmentHalfTimeStatsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MatchSimulationViewModel by activityViewModels {
        ViewModelFactory(FootballRepository(requireContext()))
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHalfTimeStatsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val homeTeamName = arguments?.getString("homeTeamName") ?: ""
        val awayTeamName = arguments?.getString("awayTeamName") ?: ""
        
        binding.tvHomeTeamName.text = homeTeamName
        binding.tvAwayTeamName.text = awayTeamName
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewModel.currentMatch.observe(viewLifecycleOwner) { match ->
            match?.let {
                binding.tvScore.text = "${it.halfTimeHomeScore} - ${it.halfTimeAwayScore}"
            }
        }
        
        viewModel.matchStatistics.observe(viewLifecycleOwner) { stats ->
            stats?.let {
                setStatRow(binding.statPossession.root, "Possession", "${it.possession.first}%", "${it.possession.second}%")
                setStatRow(binding.statShots.root, "Shots", it.shots.first.toString(), it.shots.second.toString())
                setStatRow(binding.statShotsOnTarget.root, "Shots on Target", it.shotsOnTarget.first.toString(), it.shotsOnTarget.second.toString())
                setStatRow(binding.statCorners.root, "Corners", it.corners.first.toString(), it.corners.second.toString())
                setStatRow(binding.statFouls.root, "Fouls", it.fouls.first.toString(), it.fouls.second.toString())
                setStatRow(binding.statYellowCards.root, "🟨 Yellow", it.yellowCards.first.toString(), it.yellowCards.second.toString())
                setStatRow(binding.statRedCards.root, "🟥 Red", it.redCards.first.toString(), it.redCards.second.toString())
                setStatRow(binding.statOffsides.root, "Offsides", it.offsides.first.toString(), it.offsides.second.toString())
            }
        }
    }
    
    private fun setStatRow(view: View, name: String, homeValue: String, awayValue: String) {
        view.findViewById<TextView>(R.id.tvStatName).text = name
        view.findViewById<TextView>(R.id.tvHomeValue).text = homeValue
        view.findViewById<TextView>(R.id.tvAwayValue).text = awayValue
    }
    
    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            viewModel.continueSecondHalf()
            findNavController().navigateUp()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


