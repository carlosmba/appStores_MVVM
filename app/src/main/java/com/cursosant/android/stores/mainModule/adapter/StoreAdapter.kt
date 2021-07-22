package com.cursosant.android.stores.mainModule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosant.android.stores.R
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.databinding.ItemStoreBinding

/****
 * Project: Stores
 * From: com.cursosant.android.stores
 * Created by Alain Nicolás Tello on 26/11/20 at 13:25
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
class StoreAdapter(private var stores: MutableList<StoreEntity>, private var listener: OnClickListener) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores.get(position)

        with(holder){
            setListener(store)

            binding.tvName.text = store.name
            binding.cbFavorite.isChecked = store.isFavorite

            Glide.with(mContext)
                .load(store.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int = stores.size

    fun setStores(stores: List<StoreEntity>) {
        this.stores = stores as MutableList<StoreEntity>
        notifyDataSetChanged()
    }

    fun add(storeEntity: StoreEntity) {
        Log.d("#Store", "Adapter Add ${storeEntity.id} Añadido")
        stores.add(storeEntity)
        notifyItemInserted(stores.size-1)
    }

    fun update(storeEntity: StoreEntity) {
        if(storeEntity.id != 0L){
            if(stores.contains(storeEntity)){
                val index = stores.indexOf(storeEntity)
                if (index != -1){
                    Log.d("#Store", "Adapter Update ${storeEntity.id} Actualizado")
                    stores[index] = storeEntity
                    notifyItemChanged(index)
                }
            }else{
                this.add(storeEntity)
            }
        }


    }



    fun delete(storeEntity: StoreEntity) {
        val index = stores.indexOf(storeEntity)
        if (index != -1){
            stores.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view)

        fun setListener(storeEntity: StoreEntity){
            with(binding.root) {
                setOnClickListener { listener.onClick(storeEntity) }
                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }
            }

            binding.cbFavorite.setOnClickListener{
                listener.onFavoriteStore(storeEntity)
            }
        }
    }
}