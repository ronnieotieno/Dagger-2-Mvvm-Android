package dev.ronnie.imageloaderdagger2.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import dev.ronnie.imageloaderdagger2.data.repository.Repository
import dev.ronnie.imageloaderdagger2.utils.PREF_NAME
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 *created by Ronnie Otieno on 03-Apr-21.
 **/
class SearchViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    private val sharedPreferences: SharedPreferences =
        app.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var currentResult: Flow<PagingData<ImagesResponse>>? = null

    fun searchImage(query: String): Flow<PagingData<ImagesResponse>> {
        // val orderBy = listOf("latest", "relevant").random() was returning irrelevant
        val orderBy = "relevant"
        val newResult: Flow<PagingData<ImagesResponse>> =
            repository.searchImage(query, orderBy).cachedIn(viewModelScope)
        currentResult = newResult
        setCurrentQuery(query)
        return newResult
    }

    private fun setCurrentQuery(query: String) {
        sharedPreferences.edit().apply {
            putString("query", query)
            apply()
        }
    }
    fun currentQuery(): String? {
        return sharedPreferences.getString("query", null)
    }
}