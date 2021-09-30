package com.fly.core.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        // 网格布局
        val layoutManager = parent.layoutManager
        if(layoutManager is GridLayoutManager){

            val spanCount = layoutManager.spanCount
            // 得到当前Item在RecyclerView中的位置,从0开始
            val position = parent.getChildAdapterPosition(view)
            // 通过对position加1对spanCount取余得到column
            // 保证column等于1为第一列，等于0为最后一个，其它值为中间列
            val column = (position + 1) % spanCount
            if (column == 1) {
                outRect.left = 0
            }else{
                outRect.left = space
            }

            // 得到RecyclerView中Item的总个数
            val count = parent.adapter?.itemCount?:0
            // 计算得出当前view所在的行
            val row = position / spanCount
            // 通过计算得出总行数
            val totalRow = count / spanCount + if (count % spanCount == 0) 0 else 1
            if(row == totalRow-1){
                outRect.bottom = 0
            }else{
                outRect.bottom = space
            }
        }

    }
}