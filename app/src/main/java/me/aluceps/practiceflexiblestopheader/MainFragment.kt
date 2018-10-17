package me.aluceps.practiceflexiblestopheader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.aluceps.practiceflexiblestopheader.databinding.FragmentMainBinding
import me.aluceps.practiceflexiblestopheader.databinding.ViewDummyItemBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        setData()
    }

    private fun initializeRecyclerView() {
        binding.recyclerView.run {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            isMotionEventSplittingEnabled = false
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun setData() {
        mainAdapter.run {
            clear()
            for (i in 0..100) add("number: %d".format(i))
            notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    private class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val items: MutableList<String> = mutableListOf()

        override fun onCreateViewHolder(container: ViewGroup, position: Int): RecyclerView.ViewHolder =
                DummyViewHolder(LayoutInflater.from(container.context).inflate(R.layout.view_dummy_item, container, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as DummyViewHolder).run {
                initialize(items[position])
            }
        }

        override fun getItemCount(): Int = items.size

        fun add(value: String) {
            items.add(value)
            Log.d("#####", "$value")
        }

        fun clear() {
            items.clear()
        }

        private class DummyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val binding by lazy {
                ViewDummyItemBinding.bind(itemView)
            }

            fun initialize(value: String) {
                binding.value = value
                binding.executePendingBindings()
            }
        }
    }
}