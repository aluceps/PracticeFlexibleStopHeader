package me.aluceps.practiceflexiblestopheader

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.aluceps.practiceflexiblestopheader.databinding.ViewCellHeaderBinding
import me.aluceps.practiceflexiblestopheader.databinding.ViewCellItemBinding

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                Type.Header.id -> HeaderViewHolder(LayoutInflater.from(container.context).inflate(R.layout.view_cell_header, container, false))
                else -> ItemViewHolder(LayoutInflater.from(container.context).inflate(R.layout.view_cell_item, container, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].run {
            when (type) {
                Type.Header -> (holder as HeaderViewHolder).initialize(value)
                Type.Item -> (holder as ItemViewHolder).initialize(value)
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
            val value: String
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
}
