package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(private val election: Election, private val dataSource: ElectionDao) : ViewModel() {

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    private val _selectedElection = MutableLiveData<Election>()

    val selectedElection: LiveData<Election>
        get() = _selectedElection

    private val _votingLocationFinderUrl = MutableLiveData<String>()

    val votingLocationFinderUrl: LiveData<String>
        get() = _votingLocationFinderUrl

    private val _ballotInfoUrl = MutableLiveData<String>()

    val ballotInfoUrl: LiveData<String>
        get() = _ballotInfoUrl

    private val _correspondence = MutableLiveData<Address>()

    val correspondence: LiveData<Address>
        get() = _correspondence

    private val _selectedUrl = MutableLiveData<String>()

    val selectedUrl: LiveData<String>
        get() = _selectedUrl

    val electionSaved = dataSource.getElectionWithId(election.id)

    val showToast= MutableLiveData<String>()

    init {
        _selectedElection.value = election
        getVoterInfo()
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            try {
                val adminBody = CivicsApi.retrofitService.getVoterInfo(election.id,  election.division.state).state?.get(0)?.electionAdministrationBody
                Log.d("IVS", "Success: ${adminBody} info retrieved")
                _votingLocationFinderUrl.value = adminBody?.votingLocationFinderUrl
                _ballotInfoUrl.value = adminBody?.ballotInfoUrl
                _correspondence.value = adminBody?.correspondenceAddress
            } catch (e: Exception) {
                Log.d("IVS", "$e")
            }
        }
    }

    fun clickUrl(url: String?){
        if(url != null){
            _selectedUrl.value = url
        }else{
            showToast.value = "Link not available"
        }
    }

    fun onClick(){
        viewModelScope.launch {
            if(electionSaved.value!= null){
                deleteElection()
            }else{
                saveElection()
            }
        }
    }

    private suspend fun saveElection(){
        withContext(Dispatchers.IO) {
            dataSource.insert(election)
        }
    }

    private suspend fun deleteElection(){
        withContext(Dispatchers.IO) {
            dataSource.deleteById(election.id)
        }
    }
}