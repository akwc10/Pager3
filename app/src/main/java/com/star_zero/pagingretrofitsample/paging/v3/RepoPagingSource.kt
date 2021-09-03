package com.star_zero.pagingretrofitsample.paging.v3

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.star_zero.pagingretrofitsample.api.GitHubAPI
import com.star_zero.pagingretrofitsample.data.Repo
import com.star_zero.pagingretrofitsample.ui.RepoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RepoPagingSource(
    private val api: GitHubAPI,
    private val query: String
) : PagingSource<Int, RepoItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoItem> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = requestRepos(nextPageNumber, query)
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey = if (response.isEmpty()) null else params.loadSize + PAGE_SIZE,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun requestRepos(
        page: Int,
        query: String
    ): List<RepoItem> {
        return try {
            withContext(Dispatchers.IO) {
                api.repos(query, page, PAGE_SIZE).execute()
            }.body()?.let {
                it.map { repo -> convertToItem(repo) }
            } ?: emptyList()
        } catch (e: IOException) {
            Log.w(TAG, e)
            emptyList()
        }
    }

    private fun convertToItem(repo: Repo): RepoItem = RepoItem(repo)

    override fun getRefreshKey(state: PagingState<Int, RepoItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TAG = "PageKeyedRepoDataSource"
        private const val PAGE_SIZE = 50
    }
}