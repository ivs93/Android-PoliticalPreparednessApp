package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

    val showToast= MutableLiveData<String>()

    val line1 = MutableLiveData<String>()
    val line2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val stateIndex=MutableLiveData<Int>()
    val stateString = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    private val _representatives = MutableLiveData<List<Representative>>()

    val representatives: LiveData<List<Representative>>
        get() = _representatives

    fun findRepresentatives(){
        val aLine1 = line1.value
        val aLine2 = line2.value
        val aCity = city.value
        val aState = stateString.value
        val aZip = zip.value

        try{
            val address = Address(aLine1!!, aLine2, aCity!!, aState!!, aZip!! )
            getRepresentatives(address.toFormattedString())
        }catch (e: Exception){
            showToast.value = "Please fill all the fields"
        }
    }

    private fun getRepresentatives(address: String) {
        viewModelScope.launch {
            try {
                val representativeResult =CivicsApi.retrofitService.getRepresentatives(address)
                val (offices, officials) = representativeResult
                _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
                Log.d("IVS", "Success: ${_representatives.value!!.size} Representatives retrieved")
            } catch (e: Exception) {
                Log.d("IVS", "$e")
                _representatives.value = ArrayList()

            }
        }
    }

    fun getState(state:String){
        stateString.value = state
    }



}
