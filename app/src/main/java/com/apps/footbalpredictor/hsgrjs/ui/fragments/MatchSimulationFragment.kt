package com.apps.footbalpredictor.hsgrjs.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Path
import android.view.animation.AccelerateDecelerateInterpolator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.apps.footbalpredictor.hsgrjs.R
import com.apps.footbalpredictor.hsgrjs.data.models.EventType
import com.apps.footbalpredictor.hsgrjs.data.models.MatchEvent
import com.apps.footbalpredictor.hsgrjs.data.models.Team
import com.apps.footbalpredictor.hsgrjs.data.repository.FootballRepository
import com.apps.footbalpredictor.hsgrjs.databinding.FragmentMatchSimulationBinding
import com.apps.footbalpredictor.hsgrjs.ui.viewmodel.MatchSimulationViewModel
import com.apps.footbalpredictor.hsgrjs.ui.viewmodel.ViewModelFactory

class MatchSimulationFragment : Fragment() {
    
    private var _binding: FragmentMatchSimulationBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MatchSimulationViewModel by activityViewModels {
        ViewModelFactory(FootballRepository(requireContext()))
    }
    
    private var homeTeamId: String? = null
    private var awayTeamId: String? = null
    private val activeAnimators = java.util.concurrent.CopyOnWriteArrayList<Animator>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchSimulationBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val homeTeamName = arguments?.getString("homeTeamName") ?: ""
        val awayTeamName = arguments?.getString("awayTeamName") ?: ""
        homeTeamId = arguments?.getString("homeTeamId")
        awayTeamId = arguments?.getString("awayTeamId")
        val leagueId = arguments?.getString("leagueId") ?: ""
        
        binding.tvHomeTeamName.text = homeTeamName
        binding.tvAwayTeamName.text = awayTeamName
        
        setupObservers()
        
        if (viewModel.currentMatch.value == null) {
            val homeTeam = Team(homeTeamId ?: "", homeTeamName, leagueId)
            val awayTeam = Team(awayTeamId ?: "", awayTeamName, leagueId)
            
            viewModel.startMatch(homeTeam, awayTeam, leagueId)
        }
    }
    
    private fun setupObservers() {
        viewModel.currentMatch.observe(viewLifecycleOwner) { match ->
            match?.let {
                binding.tvHomeScore.text = it.homeScore.toString()
                binding.tvAwayScore.text = it.awayScore.toString()
            }
        }
        
        viewModel.currentMinute.observe(viewLifecycleOwner) { minute ->
            binding.tvMinute.text = "$minute'"
        }
        
        viewModel.currentHalf.observe(viewLifecycleOwner) { half ->
            binding.tvHalf.text = if (half == 1) "1st Half" else "2nd Half"
        }
        
        viewModel.newEvent.observe(viewLifecycleOwner) { event ->
            event?.let { animateEvent(it) }
        }
        
        viewModel.halfTimeReached.observe(viewLifecycleOwner) { reached ->
            if (reached && viewModel.currentHalf.value == 1) {
                navigateToHalfTimeStats()
            }
        }
        
        viewModel.matchFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                navigateToMatchResult()
            }
        }
    }
    
    private fun animateEvent(event: MatchEvent) {
        val size = if (event.eventType == EventType.GOAL) 120 else 80
        
        val eventView = ImageView(requireContext()).apply {
            val drawableRes = when (event.eventType) {
                EventType.GOAL -> R.drawable.ball
                EventType.YELLOW_CARD -> R.drawable.yellow_card
                EventType.RED_CARD -> R.drawable.red_card
            }
            setImageResource(drawableRes)
            
            val params = android.widget.FrameLayout.LayoutParams(size, size)
            params.gravity = android.view.Gravity.CENTER
            layoutParams = params
        }
        
        binding.eventAnimationContainer.addView(eventView)
        
        eventView.post {
            val containerWidth = binding.eventAnimationContainer.width
            val containerHeight = binding.eventAnimationContainer.height
            
            val isHomeTeam = event.teamId == homeTeamId
            
            if (event.eventType == EventType.GOAL) {
                animateGoal(eventView, containerWidth, containerHeight, isHomeTeam)
            } else {
                val targetY = if (isHomeTeam) {
                    -(containerHeight / 2f + eventView.height / 2f)
                } else {
                    containerHeight / 2f + eventView.height / 2f
                }
                animateCard(eventView, targetY)
            }
        }
    }
    
    private fun animateGoal(eventView: ImageView, containerWidth: Int, containerHeight: Int, isHomeTeam: Boolean) {
        val targetY = if (isHomeTeam) {
            containerHeight / 2f + eventView.height / 2f
        } else {
            -(containerHeight / 2f + eventView.height / 2f)
        }
        
        val translationYAnimator = ObjectAnimator.ofFloat(eventView, "translationY", 0f, targetY)
        translationYAnimator.duration = 2500
        translationYAnimator.interpolator = AccelerateDecelerateInterpolator()
        
        val scaleAnimator = ObjectAnimator.ofFloat(eventView, "scaleX", 1f, 0.2f)
        val scaleYAnimator = ObjectAnimator.ofFloat(eventView, "scaleY", 1f, 0.2f)
        scaleAnimator.duration = 2500
        scaleYAnimator.duration = 2500
        
        val rotationAnimator = ObjectAnimator.ofFloat(eventView, "rotation", 0f, 360f * 3) // 3 rotations
        rotationAnimator.duration = 2500
        
        val alphaAnimator = ObjectAnimator.ofFloat(eventView, "alpha", 1f, 1f, 0f)
        alphaAnimator.duration = 2500
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            translationYAnimator,
            scaleAnimator,
            scaleYAnimator,
            rotationAnimator,
            alphaAnimator
        )
        
        animatorSet.startDelay = 300
        
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                try {
                    _binding?.eventAnimationContainer?.removeView(eventView)
                } catch (e: Exception) {
                }
                activeAnimators.remove(animatorSet)
            }
        })
        
        activeAnimators.add(animatorSet)
        animatorSet.start()
    }
    
    private fun animateCard(eventView: ImageView, targetY: Float) {
        val animator = ObjectAnimator.ofFloat(eventView, "translationY", 0f, targetY)
        animator.duration = 2000
        animator.startDelay = 300
        
        val alphaAnimator = ObjectAnimator.ofFloat(eventView, "alpha", 1f, 0f)
        alphaAnimator.duration = 2000
        alphaAnimator.startDelay = 300
        
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                try {
                    _binding?.eventAnimationContainer?.removeView(eventView)
                } catch (e: Exception) {
                }
                activeAnimators.remove(animator)
                activeAnimators.remove(alphaAnimator)
            }
        })
        
        activeAnimators.add(animator)
        activeAnimators.add(alphaAnimator)
        
        animator.start()
        alphaAnimator.start()
    }
    
    private fun navigateToHalfTimeStats() {
        val bundle = Bundle().apply {
            putString("homeTeamId", homeTeamId)
            putString("awayTeamId", awayTeamId)
            putString("homeTeamName", binding.tvHomeTeamName.text.toString())
            putString("awayTeamName", binding.tvAwayTeamName.text.toString())
            putString("leagueId", arguments?.getString("leagueId"))
        }
        findNavController().navigate(R.id.action_matchSimulationFragment_to_halfTimeStatsFragment, bundle)
    }
    
    private fun navigateToMatchResult() {
        val bundle = Bundle().apply {
            putString("homeTeamName", binding.tvHomeTeamName.text.toString())
            putString("awayTeamName", binding.tvAwayTeamName.text.toString())
        }
        findNavController().navigate(R.id.action_matchSimulationFragment_to_matchResultFragment, bundle)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        activeAnimators.forEach { it.cancel() }
        activeAnimators.clear()
        _binding = null
    }
}


