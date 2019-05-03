package com.cicuro.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cicuro.core.utils.disposeWith
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<T, U : BaseContext> : Fragment() {
    abstract val layoutId: Int
    abstract val presenter: BasePresenter<T, U>

    private val disposable = CompositeDisposable()

    abstract fun onInitializeView(state: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? =
        inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, state: Bundle?) {
        super.onViewCreated(view, state)
        onInitializeView(state)
        presenter.bind(this as T, (activity as BaseActivity<*>).cicuroContext as U)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbind()
        disposable.clear()
    }

    fun Disposable.disposeOnDestroy(): Disposable = apply {
        this.disposeWith(disposable)
    }
}
