package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel
    private lateinit var viewModelFactory: ElectionsViewModelFactory



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val dataSource = getInstance(requireContext()).electionDao

        viewModelFactory = ElectionsViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ElectionsViewModel::class.java)

        binding.viewModel = viewModel

        binding.recyclerUpcomingElections.adapter = ElectionListAdapter(ElectionListener {
            viewModel.displayElectionDetail(it)
        })

        binding.recyclerSavedElections.adapter = ElectionListAdapter(ElectionListener {
            viewModel.displayElectionDetail(it)
        })

        viewModel.navigateToElectionDetail.observe(viewLifecycleOwner, Observer {
            if(null != it){
                this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it))
                viewModel.displayElectionDetailComplete()
            }
        })

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}