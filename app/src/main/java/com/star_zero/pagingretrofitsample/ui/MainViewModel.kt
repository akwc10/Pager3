package com.star_zero.pagingretrofitsample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.star_zero.pagingretrofitsample.api.GitHubAPI
import com.star_zero.pagingretrofitsample.data.Repo
import com.star_zero.pagingretrofitsample.paging.RepoDataSourceFactory
import com.star_zero.pagingretrofitsample.paging.v3.RepoPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    val repos: Flow<PagedList<RepoItem>>
    val reposFlow: Flow<PagingData<RepoItem>>

//    val networkState: LiveData<NetworkState>

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(GitHubAPI::class.java)

        val factory = RepoDataSourceFactory(api, ::convertToItem)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()

        repos = LivePagedListBuilder(factory, config).build().asFlow()
//        networkState = factory.source.networkState

        reposFlow = Pager(PagingConfig(pageSize = PAGE_SIZE)) { RepoPagingSource(api, "google") }
            .flow.cachedIn(viewModelScope)
    }

    private fun convertToItem(repo: Repo): RepoItem = RepoItem(repo)

    companion object {
        private const val PAGE_SIZE = 50
    }
}
