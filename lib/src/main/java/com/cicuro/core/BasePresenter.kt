package com.cicuro.core

import com.cicuro.core.utils.disposeWith
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T, U : BaseContext> {
    private val disposable = CompositeDisposable()

    abstract fun onBindView(view: T, cicuroContext: U)

    fun bind(view: T, cicuroContext: U) {
        onBindView(view, cicuroContext)
    }

    fun unbind() {
        disposable.clear()
    }

    fun Disposable.disposeOnUnbind(): Disposable = apply {
        this.disposeWith(disposable)
    }
}
