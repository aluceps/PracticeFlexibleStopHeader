package me.aluceps.practiceflexiblestopheader

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.aluceps.practiceflexiblestopheader.databinding.ViewCellHeaderBinding
import me.aluceps.practiceflexiblestopheader.databinding.ViewCellItemBinding

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaderDecoration.StickyInterface {

    private val items: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                Type.Header.id -> HeaderViewHolder(LayoutInflater.from(container.context).inflate(R.layout.view_cell_header, container, false))
                else -> ItemViewHolder(LayoutInflater.from(container.context).inflate(R.layout.view_cell_item, container, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].run {
            when (type) {
                Type.Header -> (holder as HeaderViewHolder).initialize(text)
                Type.Item -> (holder as ItemViewHolder).initialize(text)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position].type) {
        Type.Header -> Type.Header.id
        Type.Item -> Type.Item.id
    }

    fun addHeader(value: String) {
        items.add(Item(Type.Header, value))
    }

    fun add(value: String) {
        items.add(Item(Type.Item, value))
    }

    fun clear() {
        items.clear()
    }

    private enum class Type(val id: Int) {
        Item(0),
        Header(1),
    }

    private data class Item(
            val type: Type,
            val text: String
    )

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by lazy {
            ViewCellItemBinding.bind(itemView)
        }

        fun initialize(value: String) {
            binding.value = value
            binding.executePendingBindings()
        }
    }

    private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by lazy {
            ViewCellHeaderBinding.bind(itemView)
        }

        fun initialize(value: String) {
            binding.title = value
            binding.executePendingBindings()
        }
    }

    override fun getHeaderPositionForItem(position: Int): Int {
        var headerPosition = StickyHeaderDecoration.OUT_OF_BOUNDS
        for (itemPosition in position downTo 0) {
            if (isHeader(itemPosition)) {
                headerPosition = itemPosition
                return headerPosition
            }
        }
        return headerPosition
    }

    override fun getHeaderLayout(position: Int): Int = R.layout.view_cell_header

    override fun bindHeaderData(view: View, position: Int) {
        if (position >= 0 && isHeader(position)) {
            HeaderViewHolder(view).initialize(items[position].text)
        }
    }

    override fun isHeader(position: Int): Boolean = items[position].type == Type.Header
}
