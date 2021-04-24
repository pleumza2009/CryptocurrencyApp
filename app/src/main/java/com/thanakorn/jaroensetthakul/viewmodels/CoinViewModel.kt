package com.thanakorn.jaroensetthakul.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanakorn.jaroensetthakul.models.CoinsResponse
import com.thanakorn.jaroensetthakul.repositories.CoinRepository
import com.thanakorn.jaroensetthakul.utilities.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CoinViewModel @ViewModelInject constructor(
    val coinRepository: CoinRepository
): ViewModel() {

    val coins : MutableLiveData<Resource<CoinsResponse>> = MutableLiveData()

    init {
       getCoins()
    }

    fun getCoins() = viewModelScope.launch {
        coins.postValue(Resource.Loading())
        val response = coinRepository.getCoins()
        coins.postValue(handleBreakingCoinResponse(response))
    }

    private fun handleBreakingCoinResponse(response: Response<CoinsResponse>) : Resource<CoinsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}