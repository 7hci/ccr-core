package com.cicuro.core.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposeWith(compositeDisposable: CompositeDisposable): Disposable =
    apply { compositeDisposable.add(this) }
