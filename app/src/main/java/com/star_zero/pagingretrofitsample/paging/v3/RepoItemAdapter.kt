package com.star_zero.pagingretrofitsample.paging.v3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.star_zero.pagingretrofitsample.databinding.ItemRepoBinding
import com.star_zero.pagingretrofitsample.recyclerview.EqualsDiffItemCallback
import com.star_zero.pagingretrofitsample.ui.RepoItem

class RepoItemAdapter :
    PagingDataAdapter<RepoItem, RepoItemAdapter.ViewHolder>(EqualsDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        item?.let { holder.bind(it) }
    }

    class ViewHolder(private val itemBinding: ItemRepoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(repoItem: RepoItem) {
            itemBinding.fullName.text = repoItem.repo.fullName
        }
    }
}