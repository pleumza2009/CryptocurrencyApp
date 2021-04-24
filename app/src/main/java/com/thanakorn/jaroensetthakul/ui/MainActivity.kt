package com.thanakorn.jaroensetthakul.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanakorn.jaroensetthakul.adapters.CoinAdapter
import com.thanakorn.jaroensetthakul.databinding.ActivityMainBinding
import com.thanakorn.jaroensetthakul.utilities.Resource
import com.thanakorn.jaroensetthakul.viewmodels.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel : CoinViewModel by viewModels()
    private lateinit var coinAdapter: CoinAdapter

    val TAG = "BreakingNewsFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        fetchData()



        binding.refreshLayout.setOnRefreshListener {
            fetchData()
            coinAdapter.notifyDataSetChanged()
        }



    }


     fun fetchData(){
        binding.refreshLayout.isRefreshing = true

         viewModel.coins.observe(this, Observer { response ->
             when(response){
                 is Resource.Success -> {
                     hideProgressBar()
                     response.data.let { coinResponse ->
                         binding.refreshLayout.isRefreshing = false
                         coinAdapter.differ.submitList(coinResponse?.data?.coins)
                     }
                 }
                 is Resource.Error -> {
                     binding.refreshLayout.isRefreshing = false
                     hideProgressBar()
                     response.message?.let { message ->
                         Log.e(TAG, "An error occured: $message")
                     }
                 }
                 is Resource.Loading -> {
                     showProgressBar()
                 }
             }
         })

    }



    private fun hideProgressBar() {
        binding.ProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.ProgressBar.visibility = View.VISIBLE
    }

    fun  setupRecyclerView(){
        coinAdapter = CoinAdapter()
        binding.rvCoinRanking.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}