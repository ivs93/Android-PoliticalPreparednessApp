package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
//import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ViewHolder>(ElectionDiffCallback()) {


    //TODO: Bind ViewHolder

    //TODO: Add companion object to inflate ViewHolder (from)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(clickListener,item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ItemElectionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: ElectionListener, item: Election){
            binding.election = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemElectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

class ElectionListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}


//TODO: Create ElectionViewHolder

//TODO: Create ElectionDiffCallback

//TODO: Create ElectionListener