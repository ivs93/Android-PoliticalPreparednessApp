package com.example.android.politicalpreparedness.election.adapter

import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?){
        Log.d("IVS", "Binding $data")
        val adapter = recyclerView.adapter as ElectionListAdapter
        adapter.submitList(data)
}

@BindingAdapter("textDisplayed")
fun setButtonText(button: Button, election: Election?) {
        button.text = if (election != null) button.context.getString(R.string.unfollow_election) else button.context.getString(R.string.follow_election)
}