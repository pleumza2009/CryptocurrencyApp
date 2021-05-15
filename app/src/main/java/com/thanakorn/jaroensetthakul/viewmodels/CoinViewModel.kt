package com.thanakorn.jaroensetthakul.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanakorn.jaroensetthakul.CoinApplication
import com.thanakorn.jaroensetthakul.models.CoinsResponse
import com.thanakorn.jaroensetthakul.repositories.CoinRepository
import com.thanakorn.jaroensetthakul.utilities.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CoinViewModel @ViewModelInject constructor(
    app : Application,
    val coinRepository: CoinRepository
): AndroidViewModel(app) {

    val coins : MutableLiveData<Resource<CoinsResponse>> = MutableLiveData()

    var limit = 10

    init {
       getCoins()
    }

    fun getCoins() = viewModelScope.launch {
       safeCall()
    }

    fun gerCoinsFilter() = viewModelScope.launch {
        safeCallFilter()
    }



    private fun handleCoinResponse(response: Response<CoinsResponse>) : Resource<CoinsResponse> {
        if(response.isSuccessful) {
            limit += 10
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleFilterCoinResponse(response: Response<CoinsResponse>) : Resource<CoinsResponse> {
        if(response.isSuccessful) {
            limit = 100
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    private suspend fun  safeCall(){
        coins.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = coinRepository.getCoins(limit)
                coins.postValue(handleCoinResponse(response!!))
            }else{
                coins.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> coins.postValue(Resource.Error("Network Failure"))
                else -> coins.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private suspend fun  safeCallFilter(){
        coins.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = coinRepository.getCoins(limit)
                coins.postValue(handleFilterCoinResponse(response!!))
            }else{
                coins.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> coins.postValue(Resource.Error("Network Failure"))
                else -> coins.postValue(Resource.Error("Conversion Error"))
            }

        }
    }





    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CoinApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return  false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return  when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE ->true
                    TYPE_ETHERNET-> true
                    else -> false
                }
            }
        }
        return  false
    }


}