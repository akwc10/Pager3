package com.star_zero.pagingretrofitsample.paging.v3

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.star_zero.pagingretrofitsample.recyclerview.EqualsDiffItemCallback
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

class RepoItemAdapter<T : Item<*>> : Group, GroupDataObserver {
    private var parentObserver: GroupDataObserver? = null
    private val listUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            parentObserver!!.onItemRangeInserted(this@RepoItemAdapter, position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            parentObserver!!.onItemRangeRemoved(this@RepoItemAdapter, position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            parentObserver!!.onItemMoved(this@RepoItemAdapter, fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            parentObserver!!.onItemRangeChanged(this@RepoItemAdapter, position, count)
        }
    }

    private val differ: AsyncPagingDataDiffer<T> = AsyncPagingDataDiffer(
        diffCallback = EqualsDiffItemCallback(),
        updateCallback = listUpdateCallback
    )

    private var placeHolder: Item<*>? = null

    fun setPlaceHolder(placeHolder: Item<*>?) {
        this.placeHolder = placeHolder
    }

    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    override fun getItemCount(): Int {
        return differ.itemCount
    }

    override fun getItem(position: Int): Item<*> {
        val item: Item<*>? = differ.getItem(position)
        if (item != null) {
            // TODO find more efficiency registration timing, and removing observer
            item.registerGroupDataObserver(this)
            return item
        }
        return placeHolder!!
    }

    override fun getPosition(item: Item<*>): Int {
//        val currentList = differ.currentList ?: return -1
        if (itemCount <= 0) return -1
        val currentList = List(itemCount) { getItem(it - 1) }
        return currentList.indexOf(item)
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentObserver = null
    }

    override fun onChanged(group: Group) {
        parentObserver!!.onChanged(this)
    }

    override fun onItemInserted(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemInserted(this, index)
        }
    }

    override fun onItemChanged(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemChanged(this, index)
        }
    }

    override fun onItemChanged(group: Group, position: Int, payload: Any) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemChanged(this, index, payload)
        }
    }

    override fun onItemRemoved(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemRemoved(this, index)
        }
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemRangeChanged(this, index, itemCount)
        }
    }

    override fun onItemRangeChanged(
        group: Group,
        positionStart: Int,
        itemCount: Int,
        payload: Any
    ) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemRangeChanged(this, index, itemCount, payload)
        }
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemRangeInserted(this, index, itemCount)
        }
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemRangeRemoved(this, index, itemCount)
        }
    }

    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        val index = getItemPosition(group)
        if (index >= 0 && parentObserver != null) {
            parentObserver!!.onItemRangeChanged(this, index, toPosition)
        }
    }

    override fun onDataSetInvalidated() {
        parentObserver!!.onDataSetInvalidated()
    }

    private fun getItemPosition(group: Group): Int {
//        val currentList = differ.currentList ?: return -1
        if (itemCount <= 0) return -1
        val currentList = List(itemCount) { getItem(it - 1) }
        return currentList.indexOf(group)
    }
}