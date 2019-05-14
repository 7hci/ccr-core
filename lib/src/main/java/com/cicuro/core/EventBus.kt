package com.cicuro.core

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object EventBus {
    val subject: BehaviorSubject<Event> = BehaviorSubject.create()

    inline fun <reified T : Event> register(): Observable<T> {
        return subject
            .filter { event -> event::class == T::class }
            .map { obj -> obj as T }
    }

    inline fun <reified T : Event> registerFlowable(strategy: BackpressureStrategy): Flowable<T> {
        return subject
            .filter { event -> event::class == T::class }
            .map { obj -> obj as T }
            .toFlowable(strategy)
    }

    fun post(event: Event) {
        subject.onNext(event)
    }

    interface Event
}

class ErrorEvent(val text: String) : EventBus.Event

class GenericErrorEvent : EventBus.Event

class LanguageChangedEvent : EventBus.Event