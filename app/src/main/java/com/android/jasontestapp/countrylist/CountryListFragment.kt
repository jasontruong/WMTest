package com.android.jasontestapp.countrylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.android.jasontestapp.R
import com.android.jasontestapp.network.CountryApi
import com.google.android.material.progressindicator.LinearProgressIndicator

class CountryListFragment : Fragment() {

    private val viewModel: CountryListViewModel by activityViewModels {
        CountryListViewModel.CountryListViewModelFactory(CountryApi.countryService)
    }

    private val recyclerAdapter = CountryListAdapter()
    private var recyclerView: RecyclerView? = null
    private var loadingProgressBar: LinearProgressIndicator? = null
    private var errorView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_country_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.adapter = recyclerAdapter
        loadingProgressBar = view.findViewById(R.id.loading_progress)
        errorView = view.findViewById(R.id.error_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.countryList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { _loadingState ->
            when (_loadingState) {
                LoadingStatus.LOADING -> {
                    loadingProgressBar?.visibility = View.VISIBLE
                    errorView?.visibility = View.GONE
                }
                LoadingStatus.DONE -> {
                    loadingProgressBar?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                }
                else -> {
                    loadingProgressBar?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        loadingProgressBar = null
        errorView = null
    }
}