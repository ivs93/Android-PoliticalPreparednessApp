package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val dataSource: ElectionDao): ViewModel() {

    private val _elections = MutableLiveData<List<Election>>()

    val elections: LiveData<List<Election>>
        get() = _elections

    val savedElections = dataSource.getElections()

    private val _navigateToElectionDetail = MutableLiveData<Election>()
    val navigateToElectionDetail
        get() = _navigateToElectionDetail

    init {
        getUpcomingElections()
    }

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    private fun getUpcomingElections() {
        viewModelScope.launch {
            try {
                _elections.value = CivicsApi.retrofitService.getElections().elections
                Log.d("IVS", "Success: ${_elections.value!!.size} elections retrieved")
            } catch (e: Exception) {
                Log.d("IVS", "$e")
                _elections.value = ArrayList()
            }
        }
    }

    fun displayElectionDetail(election: Election){
        _navigateToElectionDetail.value = election
    }

    fun displayElectionDetailComplete(){
        _navigateToElectionDetail.value = null
    }
}