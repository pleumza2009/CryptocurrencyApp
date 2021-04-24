package com.thanakorn.jaroensetthakul.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.OneShotPreDrawListener.add
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.Coil.imageLoader
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.thanakorn.jaroensetthakul.databinding.ListItemCoinBinding
import com.thanakorn.jaroensetthakul.databinding.ListItemCoinEvery5PositionBinding
import com.thanakorn.jaroensetthakul.models.Coin

private const val  NORMAL_TYPE_IMAGE: Int = 0
private const val  EVERY_5_POSITON_TYPE_IMAGE: Int = 1

class CoinAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   fun ImageLoader(binding: ViewBinding) : ImageLoader{
        val imageLoader = ImageLoader.Builder(binding.root.context)
           .componentRegistry {
               add(SvgDecoder(binding.root.context))
           }
           .build()
       return imageLoader
   }

    inner class  NormalViewHolder(val binding : ListItemCoinBinding): RecyclerView.ViewHolder(binding.root){
       val imageLoader = ImageLoader(binding)

        }

    inner class  Every5PositionViewHolder(val binding : ListItemCoinEvery5PositionBinding): RecyclerView.ViewHolder(binding.root){
        val imageLoader = ImageLoader(binding)

    }




    private val differCallback = object : DiffUtil.ItemCallback<Coin>(){
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.websiteUrl == newItem.websiteUrl
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == EVERY_5_POSITON_TYPE_IMAGE){
            return Every5PositionViewHolder(ListItemCoinEvery5PositionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            return NormalViewHolder(ListItemCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val coin = differ.currentList[position]
        if (getItemViewType(position) == EVERY_5_POSITON_TYPE_IMAGE){
            (holder as Every5PositionViewHolder).binding.apply {
                ivCoinViewEvery5.load(coin.iconUrl,holder.imageLoader)
                tvNameEvery5.text = coin.name
            }
        }else{
            (holder as NormalViewHolder).binding.apply {

                ivCoinView.load(coin.iconUrl,holder.imageLoader)
                tvName.text = coin.name
                val description = coin.description
                if (coin.description.isNullOrBlank()){
                    tvDescription.text = "No description\n\n\n"

                }else
                tvDescription.text = Html.fromHtml(description)
            }
            }




    }

    override fun getItemCount(): Int {
    return   differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val positivePosition = position+1
        val isEvery5Position = positivePosition % 5 == 0 && position != 0

        return if (isEvery5Position){
         EVERY_5_POSITON_TYPE_IMAGE
        }else{
            NORMAL_TYPE_IMAGE
        }
    }
}