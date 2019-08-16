package com.cicuro.core

import com.cicuro.core.utils.disposeWith
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor

abstract class BasePresenter <C: BaseContext, M> {
  private val disposable = CompositeDisposable()
  private val stateSource: FlowableProcessor<M> = BehaviorProcessor.create()

  protected abstract fun onInitialize(
    cicuroContext: C,
    stateSource: FlowableProcessor<M>,
    actions: Flowable<BaseAction>
  )

  fun initialize(
    cicuroContext: C,
    actions: Flowable<BaseAction>
  ) = onInitialize(cicuroContext, stateSource, actions)

  fun destroy() = disposable.clear()

  fun getState(): Flowable<M> = stateSource
    .onBackpressureBuffer()
    .observeOn(AndroidSchedulers.mainThread())

  fun Disposable.disposeOnDestroy(): Disposable = apply {
    this.disposeWith(disposable)
  }
}
