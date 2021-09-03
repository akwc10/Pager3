package com.star_zero.pagingretrofitsample.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.chibatching.pagedlistgroup.PagedListGroup
import com.star_zero.pagingretrofitsample.databinding.ActivityMainBinding
import com.star_zero.pagingretrofitsample.paging.v3.PagingDataGroup
import com.star_zero.pagingretrofitsample.paging.v3.RepoItemAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pagedListGroup = PagedListGroup<RepoItem>()
    private val pagingDataGroup = PagingDataGroup<RepoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel by viewModels<MainViewModel>()

        val repoItemAdapter = RepoItemAdapter()

        binding.recycler.apply {
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = GroupAdapter<GroupieViewHolder>().apply {
//                add(pagedListGroup)
                add(pagingDataGroup)
            }
//            adapter = repoItemAdapter
        }

//        lifecycleScope.launch {
//            viewModel.repos.collectLatest { pagedList ->
//                Log.d(TAG, "Receive Result")
//                pagedListGroup.submitList(pagedList)
//            }
//        }

        lifecycleScope.launch {
            viewModel.reposFlow.distinctUntilChanged().collectLatest {
                Log.d(TAG, "Receive Result")
                pagingDataGroup.submitData(it)
//                repoItemAdapter.submitData(it)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
