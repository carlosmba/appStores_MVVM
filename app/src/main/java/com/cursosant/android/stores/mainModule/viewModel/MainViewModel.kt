package com.cursosant.android.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.Constants
import com.cursosant.android.stores.mainModule.model.MainInteractor

class MainViewModel : ViewModel() {

    private var listStores : MutableList<StoreEntity> = mutableListOf()
    private var mMainInteract : MainInteractor = MainInteractor()
    private val stores : MutableLiveData<List<StoreEntity>> by lazy {
        MutableLiveData<List<StoreEntity>>().also {
            loadStores()
        }
    }
    private val isShowProgress : MutableLiveData<Boolean> = MutableLiveData()


    fun getListStores(): LiveData<List<StoreEntity>> {
        return stores
    }
    fun isShowProgress() : LiveData<Boolean>{
        return isShowProgress
    }

    private fun loadStores(){
        isShowProgress.value = Constants.SHOW
        mMainInteract.getStores{
            isShowProgress.value = Constants.HIDE
            stores.value = it
            listStores = it
        }
    }

    fun update(store: StoreEntity){
        val index = listStores.indexOf(store)
        if (index != -1){
            mMainInteract.update(store) {
                listStores[index] = it
                stores.value = listStores
            }
        }
    }

    fun delete(store: StoreEntity){
        val index = listStores.indexOf(store)
        if (index != -1){
            mMainInteract.delete(store) {
                listStores.removeAt(index)
                stores.value = listStores
            }
        }
    }

    fun favoriteStore(store: StoreEntity){
        store.isFavorite = !store.isFavorite
        this.update(store)
    }

}