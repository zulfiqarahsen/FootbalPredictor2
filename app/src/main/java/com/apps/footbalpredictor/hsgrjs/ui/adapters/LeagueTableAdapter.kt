package com.apps.footbalpredictor.hsgrjs.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.footbalpredictor.hsgrjs.data.models.LeagueTableEntry
import com.apps.footbalpredictor.hsgrjs.databinding.ItemTableRowBinding

class LeagueTableAdapter : RecyclerView.Adapter<LeagueTableAdapter.TableViewHolder>() {
    
    private var entries: List<LeagueTableEntry> = emptyList()
    
    fun submitList(newEntries: List<LeagueTableEntry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = ItemTableRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TableViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(entries[position], position + 1)
    }
    
    override fun getItemCount(): Int = entries.size
    
    class TableViewHolder(private val binding: ItemTableRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: LeagueTableEntry, position: Int) {
            binding.tvPosition.text = position.toString()
            binding.tvTeamName.text = entry.team.name
            binding.tvPlayed.text = entry.played.toString()
            binding.tvWon.text = entry.won.toString()
            binding.tvDrawn.text = entry.drawn.toString()
            binding.tvLost.text = entry.lost.toString()
            binding.tvPoints.text = entry.points.toString()
            
            val context = binding.root.context
            val resourceName = "team_${entry.team.id}"
            val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
            if (resourceId != 0) {
                binding.ivTeamIcon.setImageResource(resourceId)
            }
        }
    }
}


