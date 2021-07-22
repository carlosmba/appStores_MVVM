package com.cursosant.android.stores.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.editModule.model.EditStoreInteractor

class EditStoreViewModel : ViewModel(){
    private val storeSelected : MutableLiveData<StoreEntity> = MutableLiveData()
    private val showFab : MutableLiveData<Boolean> = MutableLiveData()
    private val storeResult : MutableLiveData<Any> = MutableLiveData()
    private val editInteractor : EditStoreInteractor = EditStoreInteractor()


    fun setStoreSelected(value : StoreEntity){
        this.storeSelected.value = value
    }

    fun getStoreSelected() : LiveData<StoreEntity>{
        return this.storeSelected
    }

    fun setShowFab(value : Boolean){
        this.showFab.value = value
    }

    fun getShowFab() : LiveData<Boolean>{
        return this.showFab
    }

    fun getStoreResult() : LiveData<Any>{
        return this.storeResult
    }

    fun setStoreResult(value : Any){
        this.storeResult.value = value
    }

    fun saveStore(store : StoreEntity){
        editInteractor.saveStore(store){
            this.storeResult.value = it
        }
    }

    fun updateStore(store : StoreEntity){
        editInteractor.updateStore(store){
            this.storeResult.value = it
        }
    }


}