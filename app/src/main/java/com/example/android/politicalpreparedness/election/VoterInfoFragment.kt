package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var viewModelFactory: VoterInfoViewModelFactory

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val election = VoterInfoFragmentArgs.fromBundle(arguments!!).selectedElection

        val dataSource = ElectionDatabase.getInstance(requireContext()).electionDao

        viewModelFactory = VoterInfoViewModelFactory(election, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.selectedUrl.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()){
                openBrowser(it)
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        return binding.root
    }

    //TODO: Create method to load URL intents
    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}