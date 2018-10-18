package me.aluceps.practiceflexiblestopheader

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class StickyHeaderDecoration(
        context: Context,
        private val orientation: Int,
        useDivider: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val rect = Rect()

    private var divider: Drawable? = null

    init {
        if (useDivider) {
            context.obtainStyledAttributes(ATTRS)?.run {
                divider = getDrawable(0)
                recycle()
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.layoutManager ?: return
        divider ?: return
        if (orientation == 1) {
            drawVertical(c, parent)
        } else {
            // You need to implementation for horizon case.
            IllegalArgumentException("Invalid orientation. It should be either VERTICAL")
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        c.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            c.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, rect)
            divider?.run {
                val bottom = rect.bottom + Math.round(child.translationY)
                val top = bottom - intrinsicHeight
                bounds = Rect(left, top, right, bottom)
                draw(c)
            }
        }
        c.restore()
    }

    fun setDrawable(drawable: Drawable) {
        this.divider = drawable
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        listener?.run {
            val top = parent.getChildAt(0) ?: return@run

            val position = parent.getChildAdapterPosition(top)
            if (position == RecyclerView.NO_POSITION) return@run

            if (getHeaderPositionForItem(position) == OUT_OF_BOUNDS) return@run
            getHeaderViewForItem(position, parent)?.let { current ->
                fixLayoutSize(parent, current)
                getChildInContact(current.bottom, parent)?.let { next ->
                    if (isHeader(parent.getChildAdapterPosition(next))) {
                        moveHeader(c, current, next)
                        return@run
                    }
                    drawHeader(c, current)
                }
            }
        }
    }

    private fun getHeaderViewForItem(position: Int, parent: RecyclerView): View? {
        listener?.run {
            val headerPosition = getHeaderPositionForItem(position)
            val headerLayoutResId = getHeaderLayout(headerPosition)
            return LayoutInflater.from(parent.context).inflate(headerLayoutResId, parent, false)?.apply {
                bindHeaderData(this, headerPosition)
            }
        } ?: return null
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)
        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun getChildInContact(point: Int, parent: RecyclerView): View? {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > point && child.top <= point) {
                return child
            }
        }
        return null
    }

    private fun moveHeader(c: Canvas, current: View, next: View) {
        c.save()
        c.translate(0F, (next.top - current.height).toFloat())
        current.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, view: View) {
        c.save()
        c.translate(0F, 0F)
        view.draw(c)
//        drawShadow(c, view)
        c.restore()
    }

    private fun drawShadow(c: Canvas, view: View) {
        view.layoutParams?.run {
            c.drawRect(0F, 0F, width.toFloat(), height.toFloat(), Paint().apply {
                setShadowLayer(10.0F, 0.0F, 2.0F, 0xff000000.toInt())
            })
        }
    }

    interface StickyInterface {

        fun getHeaderPositionForItem(position: Int): Int

        fun getHeaderLayout(position: Int): Int

        fun bindHeaderData(view: View, position: Int)

        fun isHeader(position: Int): Boolean
    }

    private var listener: StickyInterface? = null

    fun setInterface(listener: StickyInterface) {
        this.listener = listener
    }

    companion object {

        private val ATTRS = arrayOf(16843284).toIntArray()

        const val OUT_OF_BOUNDS = -1
    }
}