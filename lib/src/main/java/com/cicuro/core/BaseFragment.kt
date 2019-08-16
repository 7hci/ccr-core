package com.cicuro.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cicuro.core.utils.disposeWith
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

abstract class BaseFragment <C: BaseContext, M> : Fragment() {
  protected abstract val layoutId: Int
  private val disposable: CompositeDisposable = CompositeDisposable()
  private val actionsSource: FlowableProcessor<BaseAction> = PublishProcessor.create()
  private lateinit var presenter: BasePresenter<C, M>

  protected abstract fun getPresenter(): BasePresenter<C, M>

  protected abstract fun onInitialize(
    state: Flowable<M>,
    actionsSource: FlowableProcessor<BaseAction>,
    savedInstanceState: Bundle?
  )

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(layoutId, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val ctx: C = (activity as BaseActivity<*>).cicuroContext as C
    presenter = getPresenter()
    presenter.initialize(ctx, actionsSource.onBackpressureBuffer())
    onInitialize(presenter.getState(), actionsSource, savedInstanceState)
  }

  override fun onDestroyView() {
    presenter.destroy()
    disposable.clear()
    super.onDestroyView()
  }

  fun Disposable.disposeOnDestroy(): Disposable = apply {
    this.disposeWith(disposable)
  }
}
