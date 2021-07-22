package com.cursosant.android.stores.mainModule

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cursosant.android.stores.*
import com.cursosant.android.stores.common.utils.MainAux
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.Constants
import com.cursosant.android.stores.databinding.ActivityMainBinding
import com.cursosant.android.stores.editModule.EditStoreFragment
import com.cursosant.android.stores.editModule.viewModel.EditStoreViewModel
import com.cursosant.android.stores.mainModule.adapter.OnClickListener
import com.cursosant.android.stores.mainModule.adapter.StoreAdapter
import com.cursosant.android.stores.mainModule.viewModel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener {

    private val mEditViewModel : EditStoreViewModel by viewModels()
    private lateinit var mBinding: ActivityMainBinding
    val mMainViewModel : MainViewModel by viewModels()
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener { launchEditFragment() }

        setupViewModel()
        setupRecylcerView()

    }

    private fun setupViewModel(){
        mMainViewModel.getListStores().observe(this, { stores ->
            if(stores.isEmpty()) dialogError()
            mAdapter.setStores(stores)
        })
        mEditViewModel.getShowFab().observe(this){
            if (it) mBinding.fab.show() else mBinding.fab.hide()
        }
        mEditViewModel.getStoreSelected().observe(this){ store ->
            Log.i("#Store", "Selected adapter ${store.id}")
            mAdapter.update(store)
        }
        mMainViewModel.isShowProgress().observe(this){isShowProgress ->
            mBinding.progressBar.visibility = if(isShowProgress) View.VISIBLE else View.GONE
        }

    }
    private fun dialogError(){
        val dialog = MaterialAlertDialogBuilder(this).apply {
            title = "Ocurrio un problema al con el Servidor\nIntentalo mas tarde"
            setPositiveButton("Salir") { _, _ ->
                finish()
            }
        }
        dialog.show()
    }

    private fun launchEditFragment(store : StoreEntity = StoreEntity() ) {
        mEditViewModel.setStoreSelected(store)
        mEditViewModel.setShowFab(false)
        val editFragment = EditStoreFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.containerMain, editFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun setupRecylcerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }



    /*
    * OnClickListener
    * */
    override fun onClick(store : StoreEntity) {
        launchEditFragment(store)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        mMainViewModel.favoriteStore(storeEntity)
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val items = resources.getStringArray(R.array.array_options_item)

        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_options_title)
                .setItems(items) { _, i ->
                    when (i) {
                        0 -> confirmDelete(storeEntity)

                        1 -> dial(storeEntity.phone)

                        2 -> goToWebsite(storeEntity.website)
                    }
                }
            .show()
    }

    private fun confirmDelete(storeEntity: StoreEntity){
        mMainViewModel.delete(storeEntity)
    }

    private fun dial(phone: String){
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }

        startIntent(callIntent)
    }

    private fun goToWebsite(website: String){
        if (website.isEmpty()){
            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_LONG).show()
        } else {
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }

            startIntent(websiteIntent)
        }
    }

    private fun startIntent(intent: Intent){
        if (intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_LONG).show()
    }

    /*
    * MainAux
    * */

}