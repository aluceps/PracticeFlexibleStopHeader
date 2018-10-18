package me.aluceps.practiceflexiblestopheader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.aluceps.practiceflexiblestopheader.databinding.FragmentMainBinding

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
            addItemDecoration(StickyHeaderDecoration(context, LinearLayoutManager.VERTICAL, true).apply {
                setInterface(mainAdapter)
//                ContextCompat.getDrawable(context, R.drawable.shape_divider)?.let {
//                    setDrawable(it)
//                }
            })
        }
    }

    private fun setData() {
        mainAdapter.run {
            clear()
            for (i in 0..MAX_ITEM_COUNT) {
                when (i % 10 == 0) {
                    true -> addHeader("Section - ${i / 10}")
                    else -> add("#%d".format(i))
                }
            }
            notifyDataSetChanged()
        }
    }

    companion object {

        private const val MAX_ITEM_COUNT = 99

        fun newInstance(): Fragment = MainFragment()
    }
}