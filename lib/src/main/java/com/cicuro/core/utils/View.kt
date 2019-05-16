package com.cicuro.core.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.internal.checkMainThread
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.OnClickListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

fun <T : IItem<*, *>> FastItemAdapter<T>.itemClicks(): Observable<T> {
    return ItemClicksObservable(this)
}

private class ItemClicksObservable<T : IItem<*, *>>(
    private val adapter: FastItemAdapter<T>
) : Observable<T>() {
    override fun subscribeActual(observer: Observer<in T>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(adapter, observer)
        observer.onSubscribe(listener)
        adapter.withOnClickListener(listener)
    }

    private class Listener<T : IItem<*, *>>(
        private val adapter: FastItemAdapter<*>,
        private val observer: Observer<in T>
    ) : MainThreadDisposable(), OnClickListener<T> {
        override fun onClick(v: View?, adapter: IAdapter<T>, item: T, position: Int): Boolean {
            if (!isDisposed) {
                observer.onNext(item)
            }
            return true
        }

        override fun onDispose() {
            adapter.withOnClickListener(null)
        }
    }
}

class GridItemOffsetDecoration(
    private val offset: Int,
    private val colSpan: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        rect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = ((position) % colSpan) + 1
        val isLastColumn = column == colSpan
        val isFirstColumn = column == 1
        val row = Math.ceil((position + 1.0) / colSpan).toInt()
        val totalItems = parent.layoutManager?.itemCount ?: 0
        val isLastRow = Math.ceil(((totalItems * 1.0) / colSpan)).toInt() == row
        val isFirstRow = row == 1

        rect.left = if (isFirstColumn) offset * 2 else offset
        rect.right = if (isLastColumn) offset * 2 else offset
        rect.top = if (isFirstRow) offset * 2 else offset
        rect.bottom = if (isLastRow) offset * 2 else offset
    }
}