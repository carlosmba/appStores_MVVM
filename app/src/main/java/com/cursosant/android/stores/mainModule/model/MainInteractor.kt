package com.cursosant.android.stores.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    fun getStores(callback : (stores: MutableList<StoreEntity>) -> Unit){
        var storeList : MutableList<StoreEntity> = mutableListOf()
        val url = Constants.STORES_URL + Constants.GET_ALL_PATH
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null, {response ->
            val status = response.optInt(Constants.STATUS_PROPERTY, Constants.ERROR)
                if(status == Constants.SUCCESS){
                    Log.i("#response", "La respuesta fue $status")
                    val stores = response.optJSONArray(Constants.STORES_PROPERTY)?.toString()
                    if(stores != null) {
                        val mutableListType = object : TypeToken<MutableList<StoreEntity>>() {}.type
                        storeList = Gson().fromJson(stores, mutableListType)
                        callback(storeList)
                        return@JsonObjectRequest
                    }
                }
            callback(storeList)
        },{
            it.printStackTrace()
            callback(storeList)
        })
        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
    }


    fun getStoresRoom(callback : (stores: MutableList<StoreEntity>)-> Unit){
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callback(storesList)
            }
        }
    }

    fun update(store: StoreEntity, callback : (store: StoreEntity) -> Unit){
        doAsync {
            StoreApplication.database.storeDao().updateStore(store)
            uiThread {
                callback(store)
            }
        }
    }

    fun delete(store : StoreEntity, callback : (store : StoreEntity) -> Unit){
        doAsync {
            StoreApplication.database.storeDao().deleteStore(store)
            uiThread {
                callback(store)
            }
        }
    }
}