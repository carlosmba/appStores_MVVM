package com.cursosant.android.stores.editModule.model

import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreInteractor {

    fun saveStore(store: StoreEntity, callback : (storeId : Long) -> Unit) {
        doAsync {

            store.id = StoreApplication.database.storeDao().addStore(store)

            uiThread {
                callback(store.id)
            }
        }
    }

    fun updateStore(store : StoreEntity, callback : (store: StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().updateStore(store)
            uiThread {
                callback(store)
            }
        }
    }



}