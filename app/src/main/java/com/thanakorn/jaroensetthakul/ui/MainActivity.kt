package com.thanakorn.jaroensetthakul.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanakorn.jaroensetthakul.R
import com.thanakorn.jaroensetthakul.adapters.CoinAdapter
import com.thanakorn.jaroensetthakul.databinding.ActivityMainBinding
import com.thanakorn.jaroensetthakul.utilities.Constants.Companion.QUERY_PAGE_SIZE
import com.thanakorn.jaroensetthakul.utilities.Constants.Companion.SEARCH_TIME_DELAY
import com.thanakorn.jaroensetthakul.utilities.Resource
import com.thanakorn.jaroensetthakul.viewmodels.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
            viewModel.limit=10
            fetchData()
            coinAdapter.notifyDataSetChanged()
        }





        binding.etSearch.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
              return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()){
                    binding.rvCoinRanking.scrollToPosition(0)
                    coinAdapter.filter.filter(newText)

                }else if(newText.toString().isEmpty()){
                    viewModel.gerCoinsFilter()
                    fetchData()
                }
                else{
                    binding.rvCoinRanking.scrollToPosition(0)
                    viewModel.gerCoinsFilter()
                    fetchData()
                }
                return true
            }
        })




    }





     fun fetchData(){
         showProgressBar()
         viewModel.coins.observe(this, Observer { response ->
             when (response) {
                 is Resource.Success -> {
                     hideProgressBar()
                     response.data?.let { coinResponse ->
                         coinAdapter.differ.submitList(coinResponse.data.coins.toList())
                         if (isLastPage) {
                         binding.rvCoinRanking.setPadding(0, 0, 0, 0)
                     }
                     }
                 }
                 is Resource.Error -> {
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
        binding.refreshLayout.isRefreshing = false
        binding.ProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.refreshLayout.isRefreshing = true
        binding.ProgressBar.visibility = View.VISIBLE
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getCoins()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }






    fun  setupRecyclerView(){
        coinAdapter = CoinAdapter()
        binding.rvCoinRanking.apply {
                adapter = coinAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.recyecle_divider)!!)
            binding.rvCoinRanking.addItemDecoration(decorator)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }
}