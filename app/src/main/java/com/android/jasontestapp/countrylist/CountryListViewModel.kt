package com.android.jasontestapp.countrylist

import androidx.lifecycle.*
import com.android.jasontestapp.data.CountryData
import com.android.jasontestapp.network.CountryService
import kotlinx.coroutines.launch

enum class LoadingStatus { LOADING, DONE, ERROR }

class CountryListViewModel(private val service: CountryService): ViewModel() {

    private val _loadingLiveData = MutableLiveData<LoadingStatus>()
    val loadingLiveData: LiveData<LoadingStatus>
        get() = _loadingLiveData

    private val _countryList = MutableLiveData<List<CountryData>>()
    val countryList: LiveData<List<CountryData>>
        get() = _countryList

    init {
        loadAllCountries()
    }

    private fun loadAllCountries() {
        viewModelScope.launch {
            _loadingLiveData.value = LoadingStatus.LOADING
            try {
                _countryList.value = service.getCountries()
                _loadingLiveData.value = LoadingStatus.DONE
            } catch (exception: Exception) {
                _countryList.value = emptyList()
                _loadingLiveData.value = LoadingStatus.ERROR
            }
        }
    }

    class CountryListViewModelFactory(private val service: CountryService): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CountryListViewModel::class.java)) {
                CountryListViewModel(service) as T
            } else {
                throw (IllegalArgumentException("Unknown ViewModel class"))
            }
        }
    }
}