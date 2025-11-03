package com.apps.footbalpredictor.hsgrjs.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apps.footbalpredictor.hsgrjs.R
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository
import com.apps.footbalpredictor.hsgrjs.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment() {
    
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var repository: FootballRepository
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        repository = FootballRepository(requireContext())
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnSimulateMatch.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_matchSetupFragment)
        }
        
        binding.btnSeasonTable.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_seasonTableFragment)
        }
        
        binding.btnResetData.setOnClickListener {
            showResetConfirmationDialog()
        }
    }
    
    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Reset Data")
            .setMessage("Are you sure you want to delete all match results?")
            .setPositiveButton("Yes") { _, _ ->
                repository.clearAllData()
                AlertDialog.Builder(requireContext())
                    .setTitle("Done")
                    .setMessage("All data has been successfully deleted")
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


